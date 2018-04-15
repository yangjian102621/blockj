package com.aizone.blockchain.mine;

import com.aizone.blockchain.core.Block;
import com.aizone.blockchain.mine.pow.PowMiner;
import com.aizone.blockchain.utils.DBUtils;
import com.google.common.base.Optional;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 区块测试
 * @author yangjian
 * @since 18-4-13
 */
public class BlockTest {

	static Logger logger = LoggerFactory.getLogger(BlockTest.class);

	@Test
	public void newBlock() throws Exception {
		PowMiner powMiner = new PowMiner();
		Optional<Block> lastBlock = DBUtils.getLastBlock();
		Block block = powMiner.newBlock(lastBlock);
		//存储区块
		System.out.println(DBUtils.putBlock(block));
		System.out.println(block.getHeader());
	}

	@Test
	public void getLastBlock() {
		Optional<Block> block = DBUtils.getLastBlock();
		if (block.isPresent()) {
			logger.info("Block ====> {}", block.get().getHeader().toString());
		}
	}
}
