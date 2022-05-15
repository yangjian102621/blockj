package org.rockyang.blockj.net.base;

/**
 * Packet 消息类别, 请求为正数，响应为负数
 *
 * @author yangjian
 */
public interface MessagePacketType {

	// hello message
	byte HELLO_MESSAGE = 0;

	// new block message request
	byte REQ_NEW_BLOCK = 1;

	// new block confirm
	byte RES_NEW_BLOCK = -1;

	// new message request
	byte REQ_NEW_MESSAGE = 2;

	// new message confirm
	byte RES_NEW_MESSAGE = -2;

	// block sync request
	byte REQ_BLOCK_SYNC = 3;

	// block sync response
	byte RES_BLOCK_SYNC = -3;

	// new peer connected request
	byte REQ_NEW_PEER = 4;

	// new peer connected response
	byte RES_NEW_PEER = -4;

}
