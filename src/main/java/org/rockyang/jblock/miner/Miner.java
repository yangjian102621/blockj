package org.rockyang.jblock.miner;

import org.rockyang.jblock.chain.Block;

import java.math.BigDecimal;

/**
 * Miner interface
 * @author yangjian
 */
public interface Miner {

	BigDecimal MINING_REWARD = BigDecimal.valueOf(50);
	// mining a new block every 30s
	int BLOCK_DELAY_SECS = 30;
	String REWARD_ADDR = "B099";

	// mined a new block
	Block mineOne(Block preBlock) throws Exception;
	// 检验一个区块
	boolean validateBlock(Block block);

	Block createGenesisBlock();

}
