package org.rockyang.jblock.chain;

import org.rockyang.jblock.crypto.Hash;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Block body
 * @author yangjian
 */
public class Block implements Serializable {

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
	// block message
	private List<Message> messages;
	// signature for current block
	private String blockSign;

	public Block(List<Message> messages) {
		this.messages = messages;
	}
	public Block(int height, String previousHash)
	{
		this.height = height;
		this.previousHash = previousHash;
	}
	public Block() {
		this.messages = new ArrayList<>();
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void addMessage(Message message) {
		messages.add(message);
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

	public void setMessages(List<Message> messages)
	{
		this.messages = messages;
	}

	public String getBlockSign()
	{
		return blockSign;
	}

	public void setBlockSign(String blockSign)
	{
		this.blockSign = blockSign;
	}

	public String toSigned()
	{
		return "BlockBody{" +
				"height=" + height +
				", difficulty=" + difficulty +
				", nonce=" + nonce +
				", timestamp=" + timestamp +
				", hash='" + hash + '\'' +
				", previousHash='" + previousHash + '\'' +
				", messages=" + buildMessageSign() +
				'}';
	}

	private String buildMessageSign()
	{
		StringBuilder builder = new StringBuilder();
		messages.forEach(message -> {
			builder.append(message.getCid());
		});
		return builder.toString();
	}

	// generate block hash
	public String genBlockHash()
	{
		return Hash.sha3("BlockBody{" +
				"height=" + height +
				", difficulty=" + difficulty +
				", nonce=" + nonce +
				", timestamp=" + timestamp +
				", previousHash='" + previousHash + '\'' +
				", messages=" + buildMessageSign() +
				'}');
	}
}
