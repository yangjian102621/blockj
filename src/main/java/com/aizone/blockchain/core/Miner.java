package com.aizone.blockchain.core;

import com.aizone.blockchain.encrypt.HashUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 挖矿
 * @author yangjian
 * @since 2018-04-07 下午8:13.
 */
public class Miner {

	/**
	 * 挖矿奖励
	 */
	static final BigDecimal MINING_REWARD = BigDecimal.valueOf(50);
	/**
	 * 挖矿难度
	 */
	static final BigInteger BLOCK_DIFFICULT = new BigInteger("1000000");

	/**
	 * 简单的 PoW 算法实现，计算下个区块的 Nonce
	 * 找到一个 p1, 使得 hash(pp1) 的值以 "00000" 开头， 本次版本暂时没有加入难度计算
	 * @param lastBlock
	 * @return
	 */
	public Long proofOfWork(Block lastBlock) {
		Long nonce = 1L;
		while (!validProof(lastBlock.getHeader().getNonce(), nonce)) {
			nonce++;
		}
		return nonce;
	}

	/**
	 * 验证答案（Nonce）是否正确
	 * @param lastNonce
	 * @param nonce
	 * @return
	 */
	public boolean validProof(Long lastNonce, Long nonce) {

		String guess = HashUtils.sha256Hex(lastNonce.toString()+nonce.toString());
		return guess.startsWith("00000");
	}

	/**
	 * 挖出一个新的区块
	 * @return
	 */
	public Block newBlock(String previousBlockHash) throws Exception {
//		Block block = new Block();
//		BlockHeader header = new BlockHeader();
//		header.setIndex(1);
//		header.setNonce(100L);
//		header.setPreviousHash(previousBlockHash);
//		header.setDifficulty(BLOCK_DIFFICULT);
//		header.setTimestamp(new Date());
//		block.setHeader(header);
//		block.setHash(HashUtils.sha256Hex(header.toString()));
//		Optional<Account> coinbaseAccount = RocksDBUtils.getInstance().getCoinbaseAccount();
//		if (!coinbaseAccount.isPresent()) {
//			coinbaseAccount = Optional.of(Personal.newAccount());
//			RocksDBUtils.getInstance().putCoinbaseAccount(coinbaseAccount);
//		}
//		//生成一笔挖矿奖励
//		Transaction transaction = new Transaction(null, coinbaseAccount.get().getAddress(), MINING_REWARD);
//		BlockBody blockBody = new BlockBody();
//		blockBody.addTransaction(transaction);
//		block.setBody(blockBody);
//
//		return block;
		return null;
	}

}
