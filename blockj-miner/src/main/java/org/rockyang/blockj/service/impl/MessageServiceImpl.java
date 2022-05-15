package org.rockyang.blockj.service.impl;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.rockyang.blockj.base.crypto.Keys;
import org.rockyang.blockj.base.crypto.Sign;
import org.rockyang.blockj.base.enums.MessageStatus;
import org.rockyang.blockj.base.model.Account;
import org.rockyang.blockj.base.model.Message;
import org.rockyang.blockj.base.model.Wallet;
import org.rockyang.blockj.base.store.Datastore;
import org.rockyang.blockj.chain.MessagePool;
import org.rockyang.blockj.chain.event.NewMessageEvent;
import org.rockyang.blockj.conf.ApplicationContextProvider;
import org.rockyang.blockj.miner.Miner;
import org.rockyang.blockj.service.AccountService;
import org.rockyang.blockj.service.BlockService;
import org.rockyang.blockj.service.MessageService;
import org.rockyang.blockj.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author yangjian
 */
@Service
public class MessageServiceImpl implements MessageService {

	private final static Logger logger = LoggerFactory.getLogger(BlockService.class);

	private final Datastore datastore;
	private final AccountService accountService;
	private final WalletService walletService;
	private final MessagePool messagePool;

	public MessageServiceImpl(Datastore datastore,
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
	public boolean addMessage(Message message)
	{
		return datastore.put(MESSAGE_PREFIX + message.getCid(), message);
	}

	@Override
	public Message getMessage(String cid)
	{
		return (Message) datastore.get(MESSAGE_PREFIX + cid).orElse(null);
	}

	@Override
	public synchronized boolean validateMessage(Message message)
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
			accountService.setAccount(new Account(message.getFrom(), Miner.TOTAL_SUPPLY, null, 0));
			return true;
		}

		// transfer balance
		Account sender = accountService.getAccount(message.getFrom());
		if (sender == null) {
			logger.info("Keys not exists {}", message.getFrom());
			message.setStatus(MessageStatus.FAIL);
			return false;
		}

		// init the public key?
		if (StringUtils.isEmpty(sender.getPubKey())) {
			sender.setPubKey(message.getPubKey());
			accountService.setAccount(sender);
		}

		// check the sign
		try {
			boolean verify = Sign.verify(Keys.publicKeyDecode(message.getPubKey()), message.getSign(), message.toSigned());
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

	public synchronized String sendMessage(String from, String to, BigDecimal value, String param) throws Exception
	{

		// check the address
		Preconditions.checkArgument(to.startsWith("0x"), "Invalid format for recipient: " + from);
		Preconditions.checkArgument(StringUtils.equals(from, to), "The sender and recipient is the same address");

		BigDecimal balance = accountService.getBalance(from);
		if (balance.compareTo(value) <= 0) {
			throw new RuntimeException("insufficient balance of the sender");
		}
		// load the wallet key for from address
		Wallet senderKeys = Preconditions.checkNotNull(walletService.getWallet(from), "local keys not exists: " + from);
		Account sender = Preconditions.checkNotNull(accountService.getAccount(from), "keys not exists: " + from);

		// build the message
		Message message = new Message(from, to, value, sender.getMessageNonce() + 1);
		message.setStatus(MessageStatus.APPENDING);
		message.setParams(param);
		message.setPubKey(senderKeys.getPubKey());
		message.setStatus(MessageStatus.APPENDING);
		message.setCid(message.genMsgCid());

		if (messagePool.hasMessage(message)) {
			throw new RuntimeException("message is exists, do not resend it");
		}

		// @TODO: check the wallet nonce > message nonce

		// sign the message
		String sign = Sign.sign(senderKeys.getPriKey(), message.toSigned());
		message.setSign(sign);

		// check the signature
		if (!Sign.verify(Keys.publicKeyDecode(message.getPubKey()), sign, message.toSigned())) {
			throw new RuntimeException("signature verification failed.");
		}
		// appending to message pool
		messagePool.pendingMessage(message);

		ApplicationContextProvider.publishEvent(new NewMessageEvent(message));
		return message.getCid();
	}
}
