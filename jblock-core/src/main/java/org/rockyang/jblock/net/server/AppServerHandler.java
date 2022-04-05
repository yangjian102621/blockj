package org.rockyang.jblock.net.server;

import org.rockyang.jblock.chain.sync.ServerHandler;
import org.rockyang.jblock.net.base.BaseHandler;
import org.rockyang.jblock.net.base.MessagePacket;
import org.rockyang.jblock.net.base.MessagePacketType;
import org.rockyang.jblock.utils.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.core.intf.Packet;
import org.tio.server.intf.TioServerHandler;

/**
 * @author yangjian
 */
@Component
public class AppServerHandler extends BaseHandler implements TioServerHandler {

	private static final Logger logger = LoggerFactory.getLogger(AppServerHandler.class);

	private final ServerHandler handler;

	public AppServerHandler(ServerHandler response)
	{
		this.handler = response;
	}

	// message handler for socket server
	@Override
	public void handler(Packet packet, ChannelContext channelContext) throws Exception
	{
		MessagePacket messagePacket = (MessagePacket) packet;
		byte type = messagePacket.getType();
		byte[] body = messagePacket.getBody();
		if (body == null) {
			logger.debug("Invalid message, client: {}, drop it.", channelContext.getClientNode());
			return;
		}

		MessagePacket resPacket = null;
		switch (type) {
			case MessagePacketType.STRING_MESSAGE:
				logger.info("Receive a message: {}", body);
				resPacket = new MessagePacket(SerializeUtils.serialize(MessagePacket.HELLO_MESSAGE));
				break;

			// 同步下一个区块
			case MessagePacketType.REQ_BLOCK_SYNC:
//						resPacket = this.fetchNextBlock(body);
				break;

			// new block confirm
			case MessagePacketType.REQ_NEW_BLOCK:
				resPacket = handler.newBlock(body);
				break;

			//获取节点列表
			case MessagePacketType.REQ_PEER_LIST:
//				resPacket = this.getNodeList(body);
				break;
		} //end of switch

		//发送消息
		Tio.send(channelContext, resPacket);
	}


}
