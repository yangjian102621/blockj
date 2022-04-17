package org.rockyang.jblock.base.model;

import org.rockyang.jblock.base.crypto.Hash;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Block header
 *
 * @author yangjian
 */
public class BlockHeader implements Serializable {

	private long height;
	// pow 难度指标
	private BigInteger difficulty;
	// PoW 问题的答案
	private long nonce;
	private long timestamp = System.currentTimeMillis();
	// current block hash value
	private String hash;
	//  previous block hash value
	private String previousHash;

	public BlockHeader()
	{
	}

	public BlockHeader(long height, String previousHash)
	{
		this.height = height;
		this.previousHash = previousHash;
	}

	public long getHeight()
	{
		return height;
	}

	public void setHeight(long height)
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

	public long getNonce()
	{
		return nonce;
	}

	public void setNonce(long nonce)
	{
		this.nonce = nonce;
	}

	public long getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(long timestamp)
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

	public String genHash()
	{
		return Hash.sha3("BlockHeader{" +
				"height=" + height +
				", difficulty=" + difficulty +
				", nonce=" + nonce +
				", timestamp=" + timestamp +
				", previousHash='" + previousHash + '\'' +
				'}');
	}
}
