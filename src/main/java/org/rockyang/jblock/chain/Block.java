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
	private Long timestamp;
	private String cid;
	private String parentCid;
	// block message
	private List<Message> messages;
	// signature for current block
	private String blockSign;

	public Block(List<Message> messages) {
		this.messages = messages;
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

	public String getCid()
	{
		return cid;
	}

	public void setCid(String cid)
	{
		this.cid = cid;
	}

	public String getParentCid()
	{
		return parentCid;
	}

	public void setParentCid(String parentCid)
	{
		this.parentCid = parentCid;
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
				", cid='" + cid + '\'' +
				", parentCid='" + parentCid + '\'' +
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

	// generate block CID
	public String genBlockCid()
	{
		return Hash.sha3(this.toSigned());
	}
}
