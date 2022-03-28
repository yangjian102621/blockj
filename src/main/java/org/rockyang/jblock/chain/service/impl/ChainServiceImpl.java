package org.rockyang.jblock.chain.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.rockyang.jblock.chain.Block;
import org.rockyang.jblock.chain.Message;
import org.rockyang.jblock.chain.service.ChainService;
import org.rockyang.jblock.db.Datastore;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author yangjian
 */
@Service
public class ChainServiceImpl implements ChainService {

	public final static String CHAIN_HEAD_KEY = "block/head";
	public final static String BLOCK_PREFIX = "/blocks/";
	public final static String MESSAGE_PREFIX = "/messages/";

	private final Datastore datastore;

	public ChainServiceImpl(Datastore datastore)
	{
		this.datastore = datastore;
	}

	@Override
	public Object chainHead()
	{
		return datastore.get(CHAIN_HEAD_KEY).orElse(null);
	}

	@Override
	public void setChainHead(Object blockIndex)
	{
		datastore.put(CHAIN_HEAD_KEY, blockIndex);
	}

	@Override
	public void addBlock(Block block)
	{
		datastore.put(BLOCK_PREFIX + block.getHeader().getHeight(), block);
		// add search index for block hash
		datastore.put(BLOCK_PREFIX + block.getHeader().getHash(), block.getHeader().getHeight());

		// add index for messages in block
		block.getMessages().forEach(message -> {
			datastore.put(MESSAGE_PREFIX + message.getCid(), message);
		});

	}

	@Override
	public Block getBlockByHash(String blockHash)
	{
		Optional<Object> o = datastore.get(BLOCK_PREFIX + blockHash);
		return o.map(this::getBlock).orElse(null);
	}

	@Override
	public Block getBlock(Object blockIndex)
	{
		Optional<Object> o = datastore.get(BLOCK_PREFIX + blockIndex);
		return (Block) o.orElse(null);
	}

	@Override
	public Message getMessage(String cid)
	{
		if (cid == null) {
			return null;
		}
		// find the block which the message belongs to
		Optional<Object> blockCid = datastore.get(MESSAGE_PREFIX + cid);
		if (blockCid.isEmpty()) {
			return null;
		}
		Block block = getBlock(String.valueOf(blockCid.get()));
		for (Message message : block.getMessages()) {
			if (StringUtils.equals(message.getCid(), cid)) {
				return message;
			}
		}
		return null;
	}
}
