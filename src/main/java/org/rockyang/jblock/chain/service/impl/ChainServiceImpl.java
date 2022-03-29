package org.rockyang.jblock.chain.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.rockyang.jblock.chain.Block;
import org.rockyang.jblock.chain.Message;
import org.rockyang.jblock.chain.service.ChainService;
import org.rockyang.jblock.db.Datastore;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author yangjian
 */
@Service
public class ChainServiceImpl implements ChainService {

	public final static String CHAIN_HEAD_KEY = "block/head";
	public final static String BLOCK_PREFIX = "/blocks/";
	public final static String MESSAGE_PREFIX = "/messages/";
	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private final Lock readLock = rwl.readLock();
	private final Lock writeLock = rwl.writeLock();

	private final Datastore datastore;

	public ChainServiceImpl(Datastore datastore)
	{
		this.datastore = datastore;
	}

	@Override
	public Object chainHead()
	{
		readLock.lock();
		Optional<Object> o = datastore.get(CHAIN_HEAD_KEY);
		readLock.unlock();
		return o.orElse(null);
	}

	@Override
	public void setChainHead(Object blockIndex)
	{
		writeLock.lock();
		datastore.put(CHAIN_HEAD_KEY, blockIndex);
		writeLock.unlock();
	}

	@Override
	public void addBlock(Block block)
	{
		writeLock.lock();
		datastore.put(BLOCK_PREFIX + block.getHeader().getHeight(), block);
		// add search index for block hash
		datastore.put(BLOCK_PREFIX + block.getHeader().getHash(), block.getHeader().getHeight());

		// add index for messages in block
		block.getMessages().forEach(message -> {
			datastore.put(MESSAGE_PREFIX + message.getCid(), message);
		});
		writeLock.unlock();
	}

	@Override
	public Block getBlockByHash(String blockHash)
	{
		readLock.lock();
		Optional<Object> o = datastore.get(BLOCK_PREFIX + blockHash);
		Block block = o.map(this::getBlock).orElse(null);
		readLock.unlock();
		return block;
	}

	@Override
	public Block getBlock(Object blockIndex)
	{
		readLock.lock();
		Optional<Object> o = datastore.get(BLOCK_PREFIX + blockIndex);
		Block block = (Block) o.orElse(null);
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
}
