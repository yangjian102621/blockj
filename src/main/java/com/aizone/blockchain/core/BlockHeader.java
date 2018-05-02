package com.aizone.blockchain.core;

import com.aizone.blockchain.encrypt.HashUtils;

import java.io.Serializable;
import java.math.BigInteger;

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
	private Long nonce;
	/**
	 * 时间戳
	 */
	private Long timestamp;
	/**
	 * 区块 Hash
	 */
	private String hash;
	/**
	 * 上一个区块的 hash 地址
	 */
	private String previousHash;

	public BlockHeader(Integer index, String previousHash) {
		this.index = index;
		this.timestamp = System.currentTimeMillis();
		this.previousHash = previousHash;
	}

	public BlockHeader() {
		this.timestamp = System.currentTimeMillis();
	}

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

	public Long getNonce() {
		return nonce;
	}

	public void setNonce(Long nonce) {
		this.nonce = nonce;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getPreviousHash() {
		return previousHash;
	}

	public void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	@Override
	public String toString() {
		return "BlockHeader{" +
				"index=" + index +
				", difficulty=" + difficulty +
				", nonce=" + nonce +
				", timestamp=" + timestamp +
				", hash='" + hash + '\'' +
				", previousHash='" + previousHash + '\'' +
				'}';
	}

	/**
	 * 获取区块头的 hash 值
	 * @return
	 */
	public String toHash() {
		return HashUtils.sha256Hex("BlockHeader{" +
				"index=" + index +
				", difficulty=" + difficulty +
				", nonce=" + nonce +
				", timestamp=" + timestamp +
				", previousHash='" + previousHash + '\'' +
				'}');
	}
}
