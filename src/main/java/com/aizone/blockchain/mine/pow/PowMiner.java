package com.aizone.blockchain.mine.pow;

import com.aizone.blockchain.core.Block;
import com.aizone.blockchain.core.BlockBody;
import com.aizone.blockchain.core.BlockHeader;
import com.aizone.blockchain.core.Transaction;
import com.aizone.blockchain.encrypt.HashUtils;
import com.aizone.blockchain.encrypt.SignUtils;
import com.aizone.blockchain.encrypt.WalletUtils;
import com.aizone.blockchain.mine.Miner;
import com.aizone.blockchain.db.DBUtils;
import com.aizone.blockchain.wallet.Account;
import com.google.common.base.Optional;
import org.springframework.stereotype.Component;

import java.security.KeyPair;

/**
 * PoW 挖矿算法实现
 * @author yangjian
 * @since 18-4-13
 */
@Component
public class PowMiner implements Miner {

	@Override
	public Block newBlock(Optional<Block> block) throws Exception {

		Block newBlock;
		if (block.isPresent()) {
			Block prev = block.get();
			BlockHeader header = new BlockHeader(prev.getHeader().getIndex()+1, prev.getHeader().getHash());
			BlockBody body = new BlockBody();
			newBlock = new Block(header, body);
		} else {
			//创建创世区块
			newBlock = createGenesisBlock();
		}
		//创建挖矿奖励交易
		Transaction transaction = new Transaction();
		//获取挖矿账户
		Account account;
		Optional<Account> coinBaseAccount = DBUtils.getCoinBaseAccount();
		if (coinBaseAccount.isPresent()) {
			account = coinBaseAccount.get();
		} else {
			//创建挖矿账户
			KeyPair keyPair = WalletUtils.generateKeyPair();
			account = new Account(WalletUtils.privateKeyToString(keyPair.getPrivate()), keyPair.getPublic()
					.getEncoded());
			DBUtils.putCoinBaseAccount(Optional.of(account));
		}
		transaction.setRecipient(account.getAddress());
		transaction.setPublicKey(account.getPublicKey());
		transaction.setData("Miner Reward.");
		transaction.setTxHash(transaction.hash());
		transaction.setAmount(Miner.MINING_REWARD);
		transaction.setSign(SignUtils.sign(account.getPrivateKey(), transaction.toString()));

		//如果不是创世区块，则使用工作量证明挖矿
		if (block.isPresent()) {
			ProofOfWork proofOfWork = ProofOfWork.newProofOfWork(block.get());
			PowResult result = proofOfWork.run();
			newBlock.getHeader().setDifficulty(result.getTarget());
			newBlock.getHeader().setNonce(result.getNonce());
			newBlock.getHeader().setHash(result.getHash());
		}
		newBlock.getBody().addTransaction(transaction);

		//更新最后一个区块索引
		DBUtils.putLastBlockIndex(newBlock.getHeader().getIndex());
		return newBlock;
	}

	/**
	 * 创建创世区块
	 * @return
	 */
	private Block createGenesisBlock() {

		BlockHeader header = new BlockHeader(1, null);
		header.setNonce(PowMiner.GENESIS_BLOCK_NONCE);
		header.setDifficulty(ProofOfWork.getTarget());
		header.setHash(HashUtils.sha256Hex(header.toString()));
		BlockBody body = new BlockBody();
		return new Block(header, body);
	}

	@Override
	public boolean validateBlock(Block block) {
		ProofOfWork proofOfWork = ProofOfWork.newProofOfWork(block);
		return proofOfWork.validate();
	}
}
