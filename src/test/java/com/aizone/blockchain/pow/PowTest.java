package com.aizone.blockchain.pow;

import com.aizone.blockchain.core.Block;
import com.aizone.blockchain.core.BlockBody;
import com.aizone.blockchain.core.BlockHeader;
import com.aizone.blockchain.core.Transaction;
import com.aizone.blockchain.encrypt.HashUtils;
import com.aizone.blockchain.encrypt.SignUtils;
import com.aizone.blockchain.mine.pow.PowResult;
import com.aizone.blockchain.mine.pow.ProofOfWork;
import com.aizone.blockchain.wallet.Account;
import com.aizone.blockchain.wallet.Personal;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * 工作量证明测试
 * @author yangjian
 * @since 18-4-11
 */
public class PowTest {

	static Logger logger = LoggerFactory.getLogger(PowTest.class);

	@Test
	public void main() throws Exception {

		BlockHeader header = new BlockHeader(1, null);
		BlockBody body = new BlockBody();
		Account account = Personal.newAccount();
		Transaction transaction = new Transaction(null, account.getAddress(), BigDecimal.valueOf(50));
		transaction.setData("Mining Reward");
		transaction.setPublicKey(account.getPublicKey());
		transaction.setTxHash(HashUtils.sha256Hex(transaction.toString()));
		transaction.setSign(SignUtils.sign(account.getPrivateKey(), transaction.toString()));
		body.addTransaction(transaction);

		Block block = new Block(header, body);
		ProofOfWork proofOfWork = ProofOfWork.newProofOfWork(block);
		PowResult run = proofOfWork.run();
		System.out.println(run);

	}

}
