package org.rockyang.jblock.service;

import org.rockyang.jblock.base.model.Block;
import org.rockyang.jblock.vo.Result;

/**
 * @author yangjian
 */
public interface BlockService {

	String CHAIN_HEAD_KEY = "/block/head";
	String BLOCK_PREFIX = "/blocks/";
	String BLOCK_HEIGHT_PREFIX = "/blocks/height/";
	String BLOCK_MESSAGE_PREFIX = "/block/message/";

	// get chain head block hash
	long chainHead();

	// set chain head block hash
	boolean setChainHead(long height);

	// store the block
	boolean addBlock(Block block);

	// remove block from store
	boolean deleteBlock(String blockHash);

	// get block with the specified block hash
	Block getBlock(String blockHash);

	// get block by height index
	Block getBlockByHeight(long height);

	// save block and execute messages in block
	boolean markBlockAsValidated(Block block);

	// delete block and reverse the messages
	boolean unmarkBlockAsValidated(String blockHash);

	// check if the block is validated
	boolean isBlockValidated(long height);

	boolean isBlockValidated(String blockHash);

	Result checkBlock(Block block) throws Exception;
}
