package org.rockyang.jblock.db;

import org.rockyang.jblock.chain.Block;
import org.rockyang.jblock.chain.Message;

import java.util.List;
import java.util.Optional;

/**
 * 数据库操作接口
 * @author yangjian
 * @since 18-4-10
 */
public interface Datastore {

	// get chain head block
	String chainHead();

	void setChainHead(String blockHash);

	void putBlock(Block block);

	Block getBlock(String blockHash);
	Block getBlockByHeight(int height);

	Message getMessage(String Cid);

	boolean put(String key, Object value);

	Optional<Object> get(String key);

	boolean delete(String key);

	// search in database with key prefix
	List<Object> search(String keyPrefix);

	// close the database
	void close();
}
