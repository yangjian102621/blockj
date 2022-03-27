package org.rockyang.jblock.db;

import org.rockyang.jblock.chain.Block;
import org.rockyang.jblock.chain.Message;
import org.rockyang.jblock.chain.Wallet;

import java.util.List;
import java.util.Optional;

/**
 * 数据库操作接口
 * @author yangjian
 * @since 18-4-10
 */
public interface Datastore {

	// get chain head block hash
	String chainHead();
	// set chain head block hash
	void setChainHead(String blockHash);
	// put the block on the main chain
	void putBlock(Block block);
	// get block with the specified block hash
	Block getBlock(String blockHash);
	Block getBlockByHeight(int height);

	Wallet getWallet(String address);

	Message getMessage(String Cid);

	boolean put(String key, Object value);
	Optional<Object> get(String key);
	boolean delete(String key);

	// search in database with key prefix
	List<Object> search(String keyPrefix);

	// close the database
	void close();
}
