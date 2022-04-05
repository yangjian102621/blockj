package org.rockyang.jblock.net.base;

/**
 * Packet 消息类别, 请求为正数，响应为负数
 * @author yangjian
 */
public interface MessagePacketType {

	// normal message
	byte STRING_MESSAGE = 0;

	// new block message request
	byte REQ_NEW_BLOCK = 1;

	// new block confirm
	byte RES_NEW_BLOCK = -1;

	// new message request
	byte REQ_CONFIRM_MESSAGE = 2;

	// new message confirm
	byte RES_CONFIRM_MESSAGE = -2;

	// block sync request
	byte REQ_BLOCK_SYNC = 3;

	// block sync response
	byte RES_BLOCK_SYNC = -3;

	// get node list request
	byte REQ_PEER_LIST = 4;

	// get node list response
	byte RES_PEER_LIST = -4;

}
