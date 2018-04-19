package com.aizone.blockchain.net.base;

import org.tio.core.intf.Packet;

/**
 * 网络消息数据包
 * @author yangjian
 */
public class MessagePacket extends Packet {

	/**
	 * 消息头的长度 1+4
	 */
	public static final int HEADER_LENGTH = 5;

	public static final String HELLO_MESSAGE = "连接节点成功.";
	/**
	 * 消息类别，类别值在 MessagePacketType 常量类中定义
	 */
	private byte type;

	private byte[] body;

	public MessagePacket(byte[] body) {
		this.body = body;
	}

	public MessagePacket() {
	}

	public MessagePacket(byte type) {
		this.type = type;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

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
