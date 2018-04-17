package com.aizone.blockchain.net.base;

import org.tio.core.intf.Packet;

/**
 * @author yangjian
 */
public class BlockPacket extends Packet {

	/**
	 * packet header length
	 */
	public static final int HEADER_LENGTH = 4;

	public static final String CHARSET = "UTF-8";

	private byte[] body;

	/**
	 * @return
	 */
	public byte[] getBody() {
		return body;
	}

	/**
	 * @param body
	 */
	public void setBody(byte[] body) {
		this.body = body;
	}

}
