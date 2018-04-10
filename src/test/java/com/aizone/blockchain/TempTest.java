package com.aizone.blockchain;

import com.aizone.blockchain.core.Transaction;
import org.junit.Test;

/**
 * 临时测试文件，测试各种其他测试代码
 * @author yangjian
 * @since 2018-04-07 下午8:38.
 */
public class TempTest {

	@Test
	public void run() {
//		Block block = new Block();
//		block.setNonce(12345);
//		Miner miner = new Miner();
//		Integer nonce = miner.proofOfWork(block);
//		System.out.println(nonce);

//		ArrayList<Object> objects = new ArrayList<>();
//		objects.add(1);
//		objects.add(2);
//		objects.add(3);
//		System.out.println(objects.get(-1));

		Transaction transaction = new Transaction();
		transaction.setData(123);


	}


}
