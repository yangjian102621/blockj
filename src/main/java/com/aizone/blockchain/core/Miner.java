package com.aizone.blockchain.core;

import com.aizone.blockchain.encrypt.SHAUtils;
import org.springframework.stereotype.Component;

/**
 * 挖矿
 * @author yangjian
 * @since 2018-04-07 下午8:13.
 */
@Component
public class Miner {

	/**
	 * 简单的 PoW 算法实现，计算下个区块的 Nonce
	 * 找到一个 p1, 使得 hash(pp1) 的值以 "00000" 开头， 本次版本暂时没有加入难度计算
	 * @param lastBlock
	 * @return
	 */
	public Integer proofOfWork(Block lastBlock) {
		Integer nonce = 1;
		while (!validProof(lastBlock.getNonce(), nonce)) {
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
	public boolean validProof(Integer lastNonce, Integer nonce) {

		String guess = SHAUtils.sha256(lastNonce.toString()+nonce.toString());
		return guess.startsWith("00000");
	}

}
