package com.aizone.blockchain.mine;

import com.aizone.blockchain.core.Block;
import com.google.common.base.Optional;

import java.math.BigDecimal;

/**
 * 挖矿接口
 * @author yangjian
 * @since 2018-04-07 下午8:13.
 */
public interface Miner {

	/**
	 * 挖矿奖励
	 */
	BigDecimal MINING_REWARD = BigDecimal.valueOf(50);

	/**
	 * 创世区块难度值
	 */
	Long GENESIS_BLOCK_NONCE = 100000L;

	/**
	 * 挖出一个新的区块
	 * @param block
	 * @return
	 * @throws Exception
	 */
	public Block newBlock(Optional<Block> block) throws Exception;

	/**
	 * 检验一个区块
	 * @param block
	 * @return
	 */
	public boolean validateBlock(Block block);

}
