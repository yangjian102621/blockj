package org.rockyang.jblock.chain.service;

import org.rockyang.jblock.chain.Block;
import org.rockyang.jblock.chain.Message;
import org.rockyang.jblock.chain.sync.RespVo;

import java.math.BigDecimal;

/**
 * @author yangjian
 */
public interface ChainService {

	// get chain head block hash
	long chainHead();
	// set chain head block hash
	void setChainHead(long height);
	// store the block
	void addBlock(Block block);
	// remove block from store
	void deleteBlock(String blockHash);
	// get block with the specified block hash
	Block getBlock(String blockHash);
	// get block by height index
	Block getBlockByHeight(long height);
	// get message with the specified message Cid
	Message getMessage(String cid);

	boolean validateMessage(Message message);

	// send a message and return the message Cid
	String sendMessage(String from, String to, BigDecimal value, String param) throws Exception;

	// save block and execute messages in block
	void markBlockAsValidated(Block block);
	// delete block and reverse the messages
	void unmarkBlockAsValidated(String blockHash);
	// check if the block is validated
	boolean isBlockValidated(String blockHash);

	boolean checkBlock(Block block, RespVo respVo);
}
