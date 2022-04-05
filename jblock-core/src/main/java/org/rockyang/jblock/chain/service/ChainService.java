package org.rockyang.jblock.chain.service;

import org.rockyang.jblock.chain.Block;
import org.rockyang.jblock.chain.Message;

import java.math.BigDecimal;

/**
 * @author yangjian
 */
public interface ChainService {

	// get chain head block index
	Object chainHead();
	// set chain head block index
	void setChainHead(Object blockIndex);
	// store the block
	void addBlock(Block block);
	// remove block from store
	void deleteBlock(Object blockIndex);
	// get block with the specified block hash
	Block getBlockByHash(String blockHash);
	// get block by height index
	Block getBlock(Object blockIndex);
	// get message with the specified message Cid
	Message getMessage(String cid);

	// send a message and return the message Cid
	String sendMessage(String from, String to, BigDecimal value, String param) throws Exception;

	// save block and execute messages in block
	void validateBlock(Block block) throws Exception;
	// delete block and reverse the messages
	void unValidateBlock(Object blockIndex);
	// check if the block is validated
	boolean isBlockValidated(Object blockIndex);
}
