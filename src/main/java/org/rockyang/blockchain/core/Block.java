package org.rockyang.blockchain.core;

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
	 * 确认数
	 */
	private int confirmNum = 0;

	public Block(BlockHeader header, BlockBody body) {
		this.header = header;
		this.body = body;
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

	public int getConfirmNum() {
		return confirmNum;
	}

	public void setConfirmNum(int confirmNum) {
		this.confirmNum = confirmNum;
	}

	@Override
	public String toString() {
		return "Block{" +
				"header=" + header +
				", body=" + body +
				", confirmNum=" + confirmNum +
				'}';
	}

}
