package org.rockyang.jblock.net.base;

import org.tio.core.intf.Packet;

/**
 * 网络消息数据包
 * @author yangjian
 */
public class MessagePacket extends Packet {

	// defined the length of message header
	public static final int HEADER_LENGTH = 5;
	// say hello message
	public static final String HELLO_MESSAGE = "Hello jblock.";

	// message type, defined in class MessagePacketType
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

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

}
