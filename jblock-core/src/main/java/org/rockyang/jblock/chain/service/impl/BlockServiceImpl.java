package org.rockyang.jblock.chain.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.rockyang.jblock.chain.Block;
import org.rockyang.jblock.chain.Message;
import org.rockyang.jblock.chain.service.AccountService;
import org.rockyang.jblock.chain.service.BlockService;
import org.rockyang.jblock.chain.service.MessageService;
import org.rockyang.jblock.chain.sync.RespVo;
import org.rockyang.jblock.enums.MessageStatus;
import org.rockyang.jblock.miner.pow.ProofOfWork;
import org.rockyang.jblock.store.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author yangjian
 */
@Service
public class BlockServiceImpl implements BlockService {

	private final static Logger logger = LoggerFactory.getLogger(BlockService.class);

	private final static String CHAIN_HEAD_KEY = "block/head";
	private final static String BLOCK_PREFIX = "/blocks/";
	private final static String BLOCK_MESSAGE_PREFIX = "/block/message/";
	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private final Lock readLock = rwl.readLock();
	private final Lock writeLock = rwl.writeLock();

	private final Datastore datastore;
	private final AccountService accountService;
	private final MessageService messageService;

	public BlockServiceImpl(Datastore datastore,
	                        AccountService accountService,
	                        MessageService messageService)
	{
		this.datastore = datastore;
		this.accountService = accountService;
		this.messageService = messageService;
	}

	@Override
	public long chainHead()
	{
		Optional<Object> o = datastore.get(CHAIN_HEAD_KEY);
		return o.map(value -> (long) value).orElse(-1L);
	}

	@Override
	public void setChainHead(long height)
	{
		datastore.put(CHAIN_HEAD_KEY, height);
	}

	@Override
	public void addBlock(Block block)
	{
		writeLock.lock();
		if (isBlockValidated(block.getHeader().getHash())) {
			writeLock.unlock();
			return;
		}
		datastore.put(BLOCK_PREFIX + block.getHeader().getHash(), block);
		// add search index for block height
		datastore.put(BLOCK_PREFIX + block.getHeader().getHeight(), block.getHeader().getHash());

		// add index for messages in block
		block.getMessages().forEach(message -> {
			datastore.put(BLOCK_MESSAGE_PREFIX + message.getCid(), message);
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
		// remove block
		datastore.delete(BLOCK_PREFIX + block.getHeader().getHash());
		// we should check if this height of block hash is updated
		Optional<Object> o = datastore.get(BLOCK_PREFIX + block.getHeader().getHeight());
		if (o.isPresent()) {
			String hash = (String) o.get();
			if (StringUtils.equals(hash, blockHash)) {
				datastore.delete(BLOCK_PREFIX + block.getHeader().getHeight());
			}
		}

		// delete messages in block
		block.getMessages().forEach(message -> {
			datastore.delete(BLOCK_MESSAGE_PREFIX + message.getCid());
		});
		writeLock.unlock();
	}


	@Override
	public Block getBlock(String blockHash)
	{
		readLock.lock();
		Optional<Object> o = datastore.get(BLOCK_PREFIX + blockHash);
		Block block = null;
		if (o.isPresent()) {
			// fill message for block
			block = (Block) o.get();
			List<Message> messages = datastore.search(BLOCK_MESSAGE_PREFIX);
			block.setMessages(messages);
		}
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

	// save block and execute messages in block
	public void markBlockAsValidated(Block block)
	{
		writeLock.lock();
		for (Message message : block.getMessages()) {
			if (!messageService.validateMessage(message)) {
				continue;
			}

			// update the message height
			message.setHeight(block.getHeader().getHeight());
			message.setStatus(MessageStatus.SUCCESS);

			// Perform transfer operations and update account balances
			accountService.addBalance(message.getTo(), message.getValue());
			accountService.subBalance(message.getFrom(), message.getValue());

			// update the message nonce for sender
			accountService.addMessageNonce(message.getFrom(), 1);
		}
		addBlock(block);
		// update the chain head
		if (block.getHeader().getHeight() > 0) {
			long head = chainHead();
			if (head < block.getHeader().getHeight()) {
				setChainHead(block.getHeader().getHeight());
			}
		}
		writeLock.unlock();
	}

	@Override
	public void unmarkBlockAsValidated(String blockHash)
	{
		writeLock.lock();
		Block block = getBlock(blockHash);
		for (Message message : block.getMessages()) {
			if (!message.getStatus().equals(MessageStatus.SUCCESS)) {
				continue;
			}
			// reverse transfer message and update account balances
			accountService.addBalance(message.getFrom(), message.getValue());
			accountService.subBalance(message.getTo(), message.getValue());

			// update the message nonce for sender
			accountService.addMessageNonce(message.getFrom(), -1);
		}
		deleteBlock(blockHash);
		writeLock.unlock();
	}

	@Override
	public boolean isBlockValidated(String blockHash)
	{
		return getBlock(blockHash) != null;
	}


	/**
	 * check the block
	 * 1. Check if the previous block is exists, and previousHash is correct
	 * 2. Check if the pow result
	 * 3. Check if the block signature is correct
	 */
	@Override
	public boolean checkBlock(Block block, RespVo respVo)
	{
		readLock.lock();
		if (isBlockValidated(block.getHeader().getHash())) {
			return true;
		}

		// @TODO: check the genesis block?

		// check the proof of work nonce
		ProofOfWork proofOfWork = ProofOfWork.newProofOfWork(block.getHeader());
		if (!proofOfWork.validate()) {
			if (respVo != null) {
				respVo.setMessage("Invalid Pow result");
			}
			return false;
		}

		// check the prev block
		if (block.getHeader().getHeight() > 1) {
			Block prevBlock = getBlockByHeight(block.getHeader().getHeight() - 1);
			if (prevBlock == null || !StringUtils.equals(prevBlock.getHeader().getHash(), block.getHeader().getPreviousHash())) {
				if (respVo != null) {
					respVo.setMessage("Invalid previous hash");
				}
				return false;
			}
		}

		// @TODO: check the block signature
		readLock.unlock();
		return true;
	}
}
