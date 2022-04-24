package org.rockyang.jblock.miner;

import org.rockyang.jblock.base.model.Block;

import java.math.BigDecimal;

/**
 * Miner interface
 *
 * @author yangjian
 */
public interface Miner {

	BigDecimal MINING_REWARD = BigDecimal.valueOf(50);
	BigDecimal GENESIS_ACCOUNT_BALANCE = BigDecimal.valueOf(10000);
	// total supply coins number
	BigDecimal TOTAL_SUPPLY = BigDecimal.valueOf(21000000);
	// mining a new block every 30s
	int BLOCK_DELAY_SECS = 10;
	String REWARD_ADDR = "B099";

	// mined a new block
	Block mineOne(Block preBlock) throws Exception;

	// check if a block is valid
	boolean validateBlock(Block block);
}
