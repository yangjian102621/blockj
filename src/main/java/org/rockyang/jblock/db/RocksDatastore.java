package org.rockyang.jblock.db;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rockyang.jblock.chain.Block;
import org.rockyang.jblock.chain.Message;
import org.rockyang.jblock.utils.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * RocksDB datastore wrapper
 * @author yangjian
 */
@Component
public class RocksDatastore implements Datastore {

	private final static Logger logger = LoggerFactory.getLogger(RocksDatastore.class);
	public final static String CHAIN_HEAD_KEY = "block/head";
	public final static String MESSAGE_KEY_PREFIX = "message/";

	private RocksDB datastore;

	public RocksDatastore() {
		// load base dir from
		String dataPath = System.getProperty("JBLOCK_PATH");
		if (dataPath == null) {
			dataPath = System.getProperty("user.home")+"/.jblock/datastore";
		}
		try {
			File directory = new File(dataPath);
			if (!directory.exists()) {
				if (!directory.mkdirs()) {
					throw new FileNotFoundException("");
				}
			}
			datastore = RocksDB.open(new Options().setCreateIfMissing(true), dataPath);
		} catch (RocksDBException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String chainHead()
	{
		Optional<Object> o = get(CHAIN_HEAD_KEY);
		return String.valueOf(o);
	}

	@Override
	public boolean put(String key, Object value) {
		try {
			datastore.put(key.getBytes(), SerializeUtils.serialize(value));
			return true;
		} catch (Exception e) {
			logger.error("failed to save object: {}", e);
			return false;
		}
	}

	@Override
	public Optional<Object> get(String key) {
		try {
			return Optional.of(SerializeUtils.unSerialize(datastore.get(key.getBytes())));
		} catch (Exception e) {
			logger.error("failed to get object: {}", e);
			return Optional.empty();
		}
	}

	@Override
	public boolean delete(String key) {
		try {
			datastore.delete(key.getBytes());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public List<Object> search(String keyPrefix)
	{
		return null;
	}

	@Override
	public void setChainHead(String blockCid)
	{
		put(CHAIN_HEAD_KEY, blockCid);
	}

	@Override
	public void putBlock(Block block)
	{
		// save block
		put(block.getHash(), block);
		// save all messages in block
		block.getMessages().forEach(message -> {
			put(MESSAGE_KEY_PREFIX + message.getCid(), block.getHash());
		});
	}

	@Override
	public Block getBlock(String blockHash)
	{
		Optional<Object> o = get(blockHash);
		return (Block) o.orElse(null);
	}

	@Override
	public Block getBlockByHeight(int height)
	{
		String key = String.format("blocks/%d",height);
		Optional<Object> o = get(key);
		return o.map(blockHash -> getBlock(String.valueOf(blockHash))).orElse(null);
	}

	@Override
	public Message getMessage(String cid)
	{
		if (cid == null) {
			return null;
		}
		// find the block which the message belongs to
		Optional<Object> blockCid = get(MESSAGE_KEY_PREFIX + cid);
		if (blockCid.isEmpty()) {
			return null;
		}
		Block block = getBlock(String.valueOf(blockCid.get()));
		for (Message message : block.getMessages()) {
			if (cid.equals(message.getCid())) {
				return message;
			}
		}
		return null;
	}

	@Override
	public void close() {
		if (datastore != null) {
			datastore.close();
		}
	}
}
