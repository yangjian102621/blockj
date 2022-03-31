package org.rockyang.jblock.pow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.rockyang.jblock.Application;
import org.rockyang.jblock.chain.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 工作量证明测试
 * @author yangjian
 * @since 18-4-11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class PowTest {

	static Logger logger = LoggerFactory.getLogger(PowTest.class);

	@Autowired
	private Wallet wallet;

	@Test
	public void run() throws Exception {

//		BlockHeader header = new BlockHeader(1, null);
//		BlockBody body = new BlockBody();
//		ECKeyPair keyPair = Keys.createEcKeyPair();
//		Account account = personal.newAccount(keyPair);
//		Message message = new Message(null, account.getAddress(), BigDecimal.valueOf(50));
//		message.setData("Mining Reward");
//		//transaction.setPublicKey(account.getPublicKey());
//		message.setTxHash(Hash.sha3(message.toString()));
//		//transaction.setSign(Sign.sign(account.getPrivateKey(), transaction.toString()));
//		body.addTransaction(message);
//
//		Block block = new Block(header, body);
//		ProofOfWork proofOfWork = ProofOfWork.newProofOfWork(block);
//		PowResult result = proofOfWork.run();
//		logger.info("Pow result, {}", result);

	}

}
