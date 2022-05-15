package org.rockyang.blockj.base.model;

import org.rockyang.blockj.base.crypto.Hash;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Block header
 *
 * @author yangjian
 */
public class BlockHeader implements Serializable {

	private long height;
	// pow difficulty
	private BigInteger difficulty;
	// answer of PoW
	private long nonce;
	// block created time
	private long createTime;
	// block timestamp
	private long timestamp;
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
		this.createTime = System.currentTimeMillis() / 1000;
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

	public long getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(long createTime)
	{
		this.createTime = createTime;
	}

	@Override
	public String toString()
	{
		// we should ignore signature and timestamp
		return "BlockHeader{" +
				"height=" + height +
				", difficulty=" + difficulty +
				", nonce=" + nonce +
				", createTime=" + createTime +
				", hash='" + hash + '\'' +
				", previousHash='" + previousHash + '\'' +
				'}';
	}

	public String genCid()
	{
		return Hash.sha3("BlockHeader{" +
				"height=" + height +
				", difficulty=" + difficulty +
				", nonce=" + nonce +
				", createTime=" + createTime +
				", previousHash='" + previousHash + '\'' +
				'}');
	}
}
