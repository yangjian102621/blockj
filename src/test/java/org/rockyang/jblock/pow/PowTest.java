package org.rockyang.jblock.pow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.rockyang.jblock.Application;
import org.rockyang.jblock.account.Account;
import org.rockyang.jblock.account.Personal;
import org.rockyang.jblock.core.Block;
import org.rockyang.jblock.core.BlockBody;
import org.rockyang.jblock.core.BlockHeader;
import org.rockyang.jblock.core.Transaction;
import org.rockyang.jblock.crypto.ECKeyPair;
import org.rockyang.jblock.crypto.Hash;
import org.rockyang.jblock.crypto.Keys;
import org.rockyang.jblock.mine.pow.PowResult;
import org.rockyang.jblock.mine.pow.ProofOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

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
	private Personal personal;

	@Test
	public void run() throws Exception {

		BlockHeader header = new BlockHeader(1, null);
		BlockBody body = new BlockBody();
		ECKeyPair keyPair = Keys.createEcKeyPair();
		Account account = personal.newAccount(keyPair);
		Transaction transaction = new Transaction(null, account.getAddress(), BigDecimal.valueOf(50));
		transaction.setData("Mining Reward");
		//transaction.setPublicKey(account.getPublicKey());
		transaction.setTxHash(Hash.sha3(transaction.toString()));
		//transaction.setSign(Sign.sign(account.getPrivateKey(), transaction.toString()));
		body.addTransaction(transaction);

		Block block = new Block(header, body);
		ProofOfWork proofOfWork = ProofOfWork.newProofOfWork(block);
		PowResult result = proofOfWork.run();
		logger.info("Pow result, {}", result);

	}

}
