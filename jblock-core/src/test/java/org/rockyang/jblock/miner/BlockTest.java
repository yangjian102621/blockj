package org.rockyang.jblock.miner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.rockyang.jblock.Application;
import org.rockyang.jblock.store.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 区块测试
 * @author yangjian
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class BlockTest {

	static Logger logger = LoggerFactory.getLogger(BlockTest.class);

	@Autowired
	private Datastore dataStore;

	@Autowired
	private Miner miner;

	/**
	 * 挖矿
	 * @throws Exception
	 */
	@Test
	public void newBlock() throws Exception {

//		java.util.Optional<Block> lastBlock = dataStore.getLastBlock();
//		if (lastBlock.isPresent()) {
//			logger.info("Previous block ==> {}", lastBlock.get().getHeader());
//		}
//		Block block = miner.newBlock(lastBlock);
//		dataStore.putBlock(block);
//		dataStore.putLastBlockIndex(block.getHeader().getIndex());
//		logger.info("Block ====> {}", block.getHeader());
	}


	/**
	 * 获取最后一个区块
	 */
	@Test
	public void getLastBlock() {
//		Optional<Block> block = dataStore.getLastBlock();
//		block.ifPresent(value -> logger.info("Block ====> {}", value.getHeader()));
	}
}
