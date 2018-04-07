package com.aizone.blockchain.core;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * 区块对象
 * @author yangjian
 * @since 18-4-6
 */
public class Block implements Serializable {

	/**
	 * 区块高度
	 */
	private Integer index;
	/**
	 * 区块 hash 值
	 */
	private String hash;
	/**
	 * 区块所包含的交易记录
	 */
	private List<Transaction> transactions;
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

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
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
		return "Block{" +
				"index=" + index +
				", transactions=" + transactions +
				", difficulty=" + difficulty +
				", nonce=" + nonce +
				", timestamp=" + timestamp +
				", previousHash='" + previousHash + '\'' +
				'}';
	}
}
