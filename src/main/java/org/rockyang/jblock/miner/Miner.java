package org.rockyang.jblock.miner;

import org.rockyang.jblock.chain.Block;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Miner interface
 * @author yangjian
 */
public interface Miner {

	BigDecimal MINING_REWARD = BigDecimal.valueOf(50);
	String MINER_ADDR_KEY= "miner/address";
	String REWARD_ADDR = "B099";

	// mined a new block
	Block mineOne(Optional<Block> base);
	// 检验一个区块
	boolean validateBlock(Block block);

}
