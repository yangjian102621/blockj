package org.rockyang.jblock.chain;

import org.rockyang.jblock.crypto.Hash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Block
 * @author yangjian
 */
public class Block implements Serializable {

	private BlockHeader header;
	// block message
	private List<Message> messages;
	// signature for current block
	private String blockSign;

	public Block(List<Message> messages)
	{
		this.messages = messages;
	}
	public Block(BlockHeader blockHeader)
	{
		this.header = blockHeader;
		this.messages = new ArrayList<>();
	}
	public Block() {}

	public List<Message> getMessages() {
		return messages;
	}

	public void addMessage(Message message) {
		messages.add(message);
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

	public BlockHeader getHeader()
	{
		return header;
	}

	public void setHeader(BlockHeader header)
	{
		this.header = header;
	}

	private String buildMessages()
	{
		StringBuilder builder = new StringBuilder();
		messages.forEach(message -> {
			builder.append(message.getCid());
		});
		return builder.toString();
	}

	// generate block content hash
	public String genBlockHash()
	{
		return Hash.sha3("Block{" +
				"header=" + header.toString()  +
				", messages=" + buildMessages() +
				'}');
	}

	@Override
	public String toString()
	{
		return "Block{" +
				"header=" + header +
				", messages=" + buildMessages() +
				", blockSign='" + blockSign + '\'' +
				'}';
	}
}
