package org.rockyang.jblock.chain.service.impl;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.rockyang.jblock.chain.*;
import org.rockyang.jblock.chain.event.NewMessageEvent;
import org.rockyang.jblock.chain.service.AccountService;
import org.rockyang.jblock.chain.service.ChainService;
import org.rockyang.jblock.chain.service.WalletService;
import org.rockyang.jblock.crypto.Keys;
import org.rockyang.jblock.crypto.Sign;
import org.rockyang.jblock.store.Datastore;
import org.rockyang.jblock.enums.MessageStatus;
import org.rockyang.jblock.miner.Miner;
import org.rockyang.jblock.net.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author yangjian
 */
@Service
public class ChainServiceImpl implements ChainService {

	private final static Logger logger = LoggerFactory.getLogger(ChainService.class);

	public final static String CHAIN_HEAD_KEY = "block/head";
	public final static String BLOCK_PREFIX = "/blocks/";
	public final static String MESSAGE_PREFIX = "/messages/";
	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private final Lock readLock = rwl.readLock();
	private final Lock writeLock = rwl.writeLock();

	private final Datastore datastore;
	private final AccountService accountService;
	private final WalletService walletService;
	private final MessagePool messagePool;

	public ChainServiceImpl(Datastore datastore,
	                        AccountService accountService,
	                        WalletService walletService,
	                        MessagePool messagePool)
	{
		this.datastore = datastore;
		this.accountService = accountService;
		this.walletService = walletService;
		this.messagePool = messagePool;
	}

	@Override
	public long chainHead()
	{
		readLock.lock();
		Optional<Object> o = datastore.get(CHAIN_HEAD_KEY);
		readLock.unlock();
		return (long) o.orElse(-1);
	}

	@Override
	public void setChainHead(long height)
	{
		writeLock.lock();
		datastore.put(CHAIN_HEAD_KEY, height);
		writeLock.unlock();
	}

	@Override
	public void addBlock(Block block)
	{
		writeLock.lock();
		datastore.put(BLOCK_PREFIX + block.getHeader().getHash(), block);
		// add search index for block height
		datastore.put(BLOCK_PREFIX + block.getHeader().getHeight(), block.getHeader().getHash());

		// add index for messages in block
		block.getMessages().forEach(message -> {
			datastore.put(MESSAGE_PREFIX + message.getCid(), message);
		});
		writeLock.unlock();
	}

	@Override
	public void deleteBlock(String blockHash)
	{
		writeLock.lock();
		Block block = getBlock(blockHash);
		if (block == null) {
			writeLock.unlock();
			return;
		}
		datastore.delete(BLOCK_PREFIX + block.getHeader().getHash());
		datastore.delete(BLOCK_PREFIX + block.getHeader().getHeight());

		// delete messages in block
		block.getMessages().forEach(message -> {
			datastore.delete(MESSAGE_PREFIX + message.getCid());
		});
		writeLock.unlock();
	}


	@Override
	public Block getBlock(String blockHash)
	{
		readLock.lock();
		Optional<Object> o = datastore.get(BLOCK_PREFIX + blockHash);
		Block block = (Block) o.orElse(null);
		readLock.unlock();
		return block;
	}

	@Override
	public Block getBlockByHeight(long height)
	{
		readLock.lock();
		Optional<Object> o = datastore.get(BLOCK_PREFIX + height);
		Block block = o.map(v -> getBlock((String) v)).orElse(null);
		readLock.unlock();
		return block;
	}

	@Override
	public Message getMessage(String cid)
	{
		readLock.lock();
		if (cid == null) {
			return null;
		}
		// find the block which the message belongs to
		Optional<Object> blockCid = datastore.get(MESSAGE_PREFIX + cid);
		if (blockCid.isEmpty()) {
			readLock.unlock();
			return null;
		}
		Block block = getBlock(String.valueOf(blockCid.get()));
		for (Message message : block.getMessages()) {
			if (StringUtils.equals(message.getCid(), cid)) {
				readLock.unlock();
				return message;
			}
		}
		readLock.unlock();
		return null;
	}

	@Override
	public boolean validateMessage(Message message)
	{
		Account recipient = accountService.getAccount(message.getTo());

		// init the new address
		if (recipient == null) {
			logger.info("save a new address: {}", message.getTo());
			recipient = new Account(message.getTo(), BigDecimal.ZERO, null, 0);
			accountService.setAccount(recipient);
		}
		// mining reward message
		if (message.getFrom().equals(Miner.REWARD_ADDR)) {
			recipient.setBalance(recipient.getBalance().add(message.getValue()));
			accountService.setAccount(recipient);
			return true;
		}

		// transfer balance
		Account sender = accountService.getAccount(message.getFrom());
		if (sender == null) {
			logger.info("Keys not exists {}", message.getFrom());
			return false;
		}

		// check the sign
		try {
			boolean verify = Sign.verify(Keys.publicKeyDecode(message.getPubKey()),message.getSign(),message.toSigned());
			if (!verify) {
				message.setStatus(MessageStatus.INVALID_SIGN);
				return false;
			}
		} catch (Exception e) {
			message.setStatus(MessageStatus.INVALID_SIGN);
			return false;
		}

		// check if the account had enough balance
		if (sender.getBalance().compareTo(message.getValue()) < 0) {
			message.setStatus(MessageStatus.INSUFFICIENT_BALANCE);
			return false;
		}
		return true;
	}

	// save block and execute messages in block
	public void validateBlock(Block block)
	{
		writeLock.lock();
		for (Message message : block.getMessages()) {
			if (!validateMessage(message)) {
				continue;
			}

			Account recipient = accountService.getAccount(message.getTo());
			Account sender = accountService.getAccount(message.getFrom());

			// update the message height
			message.setHeight(block.getHeader().getHeight());
			message.setStatus(MessageStatus.SUCCESS);

			// Perform transfer operations and update account balances
			sender.setBalance(sender.getBalance().subtract(message.getValue()));
			recipient.setBalance(recipient.getBalance().add(message.getValue()));

			// update the message nonce for sender
			sender.setMessageNonce(sender.getMessageNonce() + 1);

			// update the accounts
			accountService.setAccount(sender);
			accountService.setAccount(recipient);
		}
		addBlock(block);
		// update the chain head
		long head = chainHead();
		if (head < block.getHeader().getHeight()) {
			setChainHead(block.getHeader().getHeight());
		}
		writeLock.unlock();
	}

	@Override
	public void unValidateBlock(String blockHash)
	{
		writeLock.lock();
		Block block = getBlock(blockHash);
		for (Message message : block.getMessages()) {
			if (!message.getStatus().equals(MessageStatus.SUCCESS)) {
				continue;
			}
			Account recipient = accountService.getAccount(message.getTo());
			Account sender = accountService.getAccount(message.getFrom());

			// reverse transfer message and update account balances
			sender.setBalance(sender.getBalance().add(message.getValue()));
			recipient.setBalance(recipient.getBalance().subtract(message.getValue()));

			// update the message nonce for sender
			sender.setMessageNonce(sender.getMessageNonce() - 1);

			// update the accounts
			accountService.setAccount(sender);
			accountService.setAccount(recipient);
		}
		deleteBlock(blockHash);
		writeLock.unlock();
	}

	@Override
	public boolean isBlockValidated(String blockHash)
	{
		readLock.lock();
		Block block = getBlock(blockHash);
		readLock.unlock();
		return block != null;
	}

	public String sendMessage(String from, String to, BigDecimal value, String param) throws Exception
	{

		// check the address
		Preconditions.checkArgument(to.startsWith("0x"), "Invalid format for recipient: " + from);
		Preconditions.checkArgument(StringUtils.equals(from, to), "The sender and recipient is the same address");

		// load the wallet key for from address
		Wallet senderKeys = Preconditions.checkNotNull(walletService.getWallet(from), "local keys not exists: " + from);
		Account sender = Preconditions.checkNotNull(accountService.getAccount(from), "keys not exists: " + from);
		// build the message
		Message message = new Message(from, to, value, sender.getMessageNonce());
		message.setStatus(MessageStatus.APPENDING);
		message.setParams(param);
		message.setCid(message.genMsgCid());
		message.setPubKey(senderKeys.getPubKey());
		// sign the message
		String sign = Sign.sign(senderKeys.getPriKey(), message.toSigned());
		message.setSign(sign);

		// check the signature
		if (!Sign.verify(Keys.publicKeyDecode(message.getPubKey()), sign, message.toSigned())) {
			throw new RuntimeException("Signature verification failed.");
		}
		// appending to message pool
		messagePool.pendingMessage(message);

		// update the sender's message nonce
		sender.setMessageNonce(sender.getMessageNonce() + 1);
		accountService.setAccount(sender);

		ApplicationContextProvider.publishEvent(new NewMessageEvent(message));
		return message.getCid();
	}
}
