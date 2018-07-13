package com.ppblock.blockchain.mine.pow;

import com.ppblock.blockchain.core.Block;
import com.ppblock.blockchain.core.BlockBody;
import com.ppblock.blockchain.core.BlockHeader;
import com.ppblock.blockchain.core.Transaction;
import com.ppblock.blockchain.db.DBAccess;
import com.ppblock.blockchain.account.Account;
import com.google.common.base.Optional;
import com.ppblock.blockchain.mine.Miner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * PoW 挖矿算法实现
 * @author yangjian
 * @since 18-4-13
 */
@Component
public class PowMiner implements Miner {

	@Autowired
	private DBAccess dbAccess;

	@Override
	public Block newBlock(Optional<Block> block) throws Exception {

		//获取挖矿账户
		Account account;
		Optional<Account> coinBaseAccount = dbAccess.getCoinBaseAccount();
		if (!coinBaseAccount.isPresent()) {
			throw new RuntimeException("没有找到挖矿账户，请先创建挖矿账户.");
		}
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

		account = coinBaseAccount.get();
		transaction.setTo(account.getAddress());
		transaction.setData("Miner Reward.");
		transaction.setTxHash(transaction.hash());
		transaction.setAmount(Miner.MINING_REWARD);

		//如果不是创世区块，则使用工作量证明挖矿
		if (block.isPresent()) {
			ProofOfWork proofOfWork = ProofOfWork.newProofOfWork(newBlock);
			PowResult result = proofOfWork.run();
			newBlock.getHeader().setDifficulty(result.getTarget());
			newBlock.getHeader().setNonce(result.getNonce());
			newBlock.getHeader().setHash(result.getHash());
		}
		newBlock.getBody().addTransaction(transaction);

		//更新最后一个区块索引
		dbAccess.putLastBlockIndex(newBlock.getHeader().getIndex());
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
		header.setHash(header.toHash());
		BlockBody body = new BlockBody();
		return new Block(header, body);
	}

	@Override
	public boolean validateBlock(Block block) {
		ProofOfWork proofOfWork = ProofOfWork.newProofOfWork(block);
		return proofOfWork.validate();
	}
}
