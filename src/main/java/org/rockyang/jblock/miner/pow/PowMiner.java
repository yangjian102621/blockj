package org.rockyang.jblock.miner.pow;

import org.rockyang.jblock.chain.Block;
import org.rockyang.jblock.db.Datastore;
import org.rockyang.jblock.miner.Miner;
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
	private Datastore dataStore;

	@Override
	public Block newBlock(java.util.Optional<Block> block) {

		//获取挖矿账户
//		Account account;
//		Optional<Account> minerAccount = dataStore.getMinerAccount();
//		if (minerAccount.isEmpty()) {
//			throw new RuntimeException("没有找到挖矿账户，请先创建挖矿账户.");
//		}
//		Block newBlock;
//		if (block.isPresent()) {
//			Block prev = block.get();
//			BlockHeader header = new BlockHeader(prev.getHeader().getIndex()+1, prev.getHeader().getHash());
//			BlockBody body = new BlockBody();
//			newBlock = new Block(header, body);
//		} else {
//			//创建创世区块
//			newBlock = createGenesisBlock();
//		}
//		//创建挖矿奖励交易
//		Message message = new Message();
//
//		account = minerAccount.get();
//		message.setTo(account.getAddress());
//		message.setData("Miner Reward.");
//		message.setTxHash(message.hash());
//		message.setAmount(Miner.MINING_REWARD);
//
//		//如果不是创世区块，则使用工作量证明挖矿
//		if (block.isPresent()) {
//			ProofOfWork proofOfWork = ProofOfWork.newProofOfWork(newBlock);
//			PowResult result = proofOfWork.run();
//			newBlock.getHeader().setDifficulty(result.getTarget());
//			newBlock.getHeader().setNonce(result.getNonce());
//			newBlock.getHeader().setHash(result.getHash());
//		}
//		newBlock.getBody().addTransaction(message);
//
//		//更新最后一个区块索引
//		dataStore.putLastBlockIndex(newBlock.getHeader().getIndex());
//		return newBlock;

		return new Block();
	}

	/**
	 * 创建创世区块
	 * @return
	 */
	private Block createGenesisBlock() {

		Block block = new Block();
		block.setNonce(PowMiner.GENESIS_BLOCK_NONCE);
		block.setDifficulty(ProofOfWork.getTarget());
		block.setHash(block.genBlockHash());
		return block;
	}

	@Override
	public boolean validateBlock(Block block) {
		ProofOfWork proofOfWork = ProofOfWork.newProofOfWork(block);
		return proofOfWork.validate();
	}
}
