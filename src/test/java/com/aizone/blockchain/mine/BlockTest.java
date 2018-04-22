package com.aizone.blockchain.mine;

import com.aizone.blockchain.Application;
import com.aizone.blockchain.core.Block;
import com.aizone.blockchain.db.DBAccess;
import com.google.common.base.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 区块测试
 * @author yangjian
 * @since 18-4-13
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class BlockTest {

	static Logger logger = LoggerFactory.getLogger(BlockTest.class);

	@Autowired
	private DBAccess dbAccess;

	@Autowired
	private Miner miner;

	/**
	 * 挖矿
	 * @throws Exception
	 */
	@Test
	public void newBlock() throws Exception {


		Optional<Block> lastBlock = dbAccess.getLastBlock();
		Block block = miner.newBlock(lastBlock);
		//存储区块
		System.out.println(dbAccess.putBlock(block));
		System.out.println(block.getHeader());
	}

	/**
	 * 获取最后一个区块
	 */
	@Test
	public void getLastBlock() {
		Optional<Block> block = dbAccess.getLastBlock();
		if (block.isPresent()) {
			logger.info("Block ====> {}", block.get().getHeader().toString());
		}
	}
}
