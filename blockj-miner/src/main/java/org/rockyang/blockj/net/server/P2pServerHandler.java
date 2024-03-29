package org.rockyang.blockj.net.server;

import org.rockyang.blockj.base.utils.SerializeUtils;
import org.rockyang.blockj.chain.sync.ServerHandler;
import org.rockyang.blockj.net.base.BaseHandler;
import org.rockyang.blockj.net.base.MessagePacket;
import org.rockyang.blockj.net.base.MessagePacketType;
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
public class P2pServerHandler extends BaseHandler implements TioServerHandler {

	private static final Logger logger = LoggerFactory.getLogger(P2pServerHandler.class);

	private final ServerHandler handler;

	public P2pServerHandler(ServerHandler response)
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
			case MessagePacketType.HELLO_MESSAGE:
				logger.info("hello message: {}", SerializeUtils.unSerialize(body));
				break;
			case MessagePacketType.REQ_BLOCK_SYNC:
				resPacket = handler.syncBlock(body);
				break;
			case MessagePacketType.REQ_NEW_BLOCK:
				resPacket = handler.newBlock(body);
				break;
			case MessagePacketType.REQ_NEW_MESSAGE:
				resPacket = handler.newMessage(body);
				break;
			case MessagePacketType.REQ_NEW_PEER:
				resPacket = handler.newPeer(body);
				break;
		}

		if (resPacket != null) {
			Tio.send(channelContext, resPacket);
		}
	}

}
