package org.rockyang.jblock.net.client;

import org.rockyang.jblock.chain.sync.ClientHandler;
import org.rockyang.jblock.net.base.BaseHandler;
import org.rockyang.jblock.net.base.MessagePacket;
import org.rockyang.jblock.net.base.MessagePacketType;
import org.rockyang.jblock.utils.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tio.client.intf.TioClientHandler;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;

/**
 * @author yangjian
 */
@Component
public class AppClientHandler extends BaseHandler implements TioClientHandler {

	private static final Logger logger = LoggerFactory.getLogger(AppClientHandler.class);

	private final ClientHandler handler;

	public AppClientHandler(ClientHandler request)
	{
		this.handler = request;
	}

	// message handler
	@Override
	public void handler(Packet packet, ChannelContext channelContext)
	{

		MessagePacket messagePacket = (MessagePacket) packet;
		byte[] body = messagePacket.getBody();
		byte type = messagePacket.getType();
		if (body != null) {
			logger.info("Received message from {}", channelContext.getServerNode());
			switch (type) {
				// a simple string message
				case MessagePacketType.STRING_MESSAGE:
					logger.info("String message: {}", SerializeUtils.unSerialize(body));
					break;

					//确认交易回复
				case MessagePacketType.RES_CONFIRM_MESSAGE:
					//this.confirmTransaction(body);
					break;

					//同步区块回复
				case MessagePacketType.RES_BLOCK_SYNC:
//					this.fetchNextBlock(body);
					break;

					// broadcast new block
				case MessagePacketType.RES_NEW_BLOCK:
					handler.newBlock(body);
					break;

					// 同步节点信息
				case MessagePacketType.RES_PEER_LIST:
//					this.getNodeList(body);
					break;

			} //end of switch

		}
	}
}
