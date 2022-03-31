package org.rockyang.jblock.chain;

import org.rockyang.jblock.crypto.Hash;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Block header
 * @author yangjian
 */
public class BlockHeader implements Serializable {

	private Integer height;
	// pow 难度指标
	private BigInteger difficulty;
	// PoW 问题的答案
	private Long nonce;
	private Long timestamp = System.currentTimeMillis();
	// current block hash value
	private String hash;
	//  previous block hash value
	private String previousHash;

	public BlockHeader() {}

	public BlockHeader(int height, String previousHash)
	{
		this.height = height;
		this.previousHash = previousHash;
	}

	public Integer getHeight()
	{
		return height;
	}

	public void setHeight(Integer height)
	{
		this.height = height;
	}

	public BigInteger getDifficulty()
	{
		return difficulty;
	}

	public void setDifficulty(BigInteger difficulty)
	{
		this.difficulty = difficulty;
	}

	public Long getNonce()
	{
		return nonce;
	}

	public void setNonce(Long nonce)
	{
		this.nonce = nonce;
	}

	public Long getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(Long timestamp)
	{
		this.timestamp = timestamp;
	}

	public String getHash()
	{
		return hash;
	}

	public void setHash(String hash)
	{
		this.hash = hash;
	}

	public String getPreviousHash()
	{
		return previousHash;
	}

	public void setPreviousHash(String previousHash)
	{
		this.previousHash = previousHash;
	}

	@Override
	public String toString()
	{
		return "BlockHeader{" +
				"height=" + height +
				", difficulty=" + difficulty +
				", nonce=" + nonce +
				", timestamp=" + timestamp +
				", hash='" + hash + '\'' +
				", previousHash='" + previousHash + '\'' +
				'}';
	}

	public String genHash() {
		return Hash.sha3("BlockHeader{" +
				"height=" + height +
				", difficulty=" + difficulty +
				", nonce=" + nonce +
				", timestamp=" + timestamp +
				", previousHash='" + previousHash + '\'' +
				'}');
	}
}
