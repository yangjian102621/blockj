package com.aizone.blockchain;

import com.aizone.blockchain.core.Block;
import com.aizone.blockchain.core.BlockChain;
import com.google.common.base.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlockchainApplicationTests {

	@Autowired
	BlockChain blockChain;

	@Test
	public void contextLoads() {

		Optional<Block> block = blockChain.getLastBlock();
		if (block.isPresent()) {
			System.out.println(block.get().getHeader());
		}

	}

}
