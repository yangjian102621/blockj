package org.rockyang.blockchain.mine.pow;

import com.google.common.base.Optional;
import org.rockyang.blockchain.account.Account;
import org.rockyang.blockchain.core.Block;
import org.rockyang.blockchain.core.BlockBody;
import org.rockyang.blockchain.core.BlockHeader;
import org.rockyang.blockchain.core.Transaction;
import org.rockyang.blockchain.db.DBAccess;
import org.rockyang.blockchain.mine.Miner;
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
		Optional<Account> minerAccount = dbAccess.getMinerAccount();
		if (!minerAccount.isPresent()) {
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

		account = minerAccount.get();
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
		header.setHash(header.hash());
		BlockBody body = new BlockBody();
		return new Block(header, body);
	}

	@Override
	public boolean validateBlock(Block block) {
		ProofOfWork proofOfWork = ProofOfWork.newProofOfWork(block);
		return proofOfWork.validate();
	}
}
