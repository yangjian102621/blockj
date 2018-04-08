package com.aizone.blockchain.core;

import java.io.Serializable;

/**
 * 区块
 * @author yangjian
 * @since 18-4-6
 */
public class Block implements Serializable {

	/**
	 * 区块 Header
	 */
	private BlockHeader header;
	/**
	 * 区块 Body
	 */
	private BlockBody body;
	/**
	 * 区块 Hash
	 */
	private String hash;

	public Block(BlockHeader header, BlockBody body, String hash) {
		this.header = header;
		this.body = body;
		this.hash = hash;
	}

	public Block() {
	}

	public BlockHeader getHeader() {
		return header;
	}

	public void setHeader(BlockHeader header) {
		this.header = header;
	}

	public BlockBody getBody() {
		return body;
	}

	public void setBody(BlockBody body) {
		this.body = body;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
}
