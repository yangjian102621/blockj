package org.rockyang.jblock.chain.service;

import org.rockyang.jblock.chain.Block;
import org.rockyang.jblock.chain.Message;

/**
 * @author yangjian
 */
public interface ChainService {

	// get chain head block index
	Object chainHead();
	// set chain head block index
	void setChainHead(Object blockIndex);
	// put the block on the main chain
	void addBlock(Block block);
	// get block with the specified block hash
	Block getBlockByHash(String blockHash);
	// get block by height index
	Block getBlock(Object blockIndex);
	// get message with the specified message Cid
	Message getMessage(String cid);
}
