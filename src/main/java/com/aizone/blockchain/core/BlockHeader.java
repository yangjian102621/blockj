package com.aizone.blockchain.core;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * 区块头
 * @author yangjian
 * @since 18-4-6
 */
public class BlockHeader implements Serializable {

	/**
	 * 区块高度
	 */
	private Integer index;
	/**
	 * 难度指标
	 */
	private BigInteger difficulty;
	/**
	 * PoW 问题的答案
	 */
	private Integer nonce;
	/**
	 * 时间戳
	 */
	private Date timestamp;
	/**
	 * 上一个区块的 hash 地址
	 */
	private String previousHash;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public BigInteger getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigInteger difficulty) {
		this.difficulty = difficulty;
	}

	public Integer getNonce() {
		return nonce;
	}

	public void setNonce(Integer nonce) {
		this.nonce = nonce;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getPreviousHash() {
		return previousHash;
	}

	public void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}

	@Override
	public String toString() {
		return "BlockHeader{" +
				"index=" + index +
				", difficulty=" + difficulty +
				", nonce=" + nonce +
				", timestamp=" + timestamp +
				", previousHash='" + previousHash + '\'' +
				'}';
	}
}
