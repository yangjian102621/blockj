package org.rockyang.blockj.net.client;

import org.rockyang.blockj.chain.sync.ClientHandler;
import org.rockyang.blockj.net.base.BaseHandler;
import org.rockyang.blockj.net.base.MessagePacket;
import org.rockyang.blockj.net.base.MessagePacketType;
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
public class P2pClientHandler extends BaseHandler implements TioClientHandler {

	private static final Logger logger = LoggerFactory.getLogger(P2pClientHandler.class);
	private final ClientHandler handler;

	public P2pClientHandler(ClientHandler handler)
	{
		this.handler = handler;
	}

	// message handler
	@Override
	public void handler(Packet packet, ChannelContext channelContext) throws Exception
	{
		MessagePacket messagePacket = (MessagePacket) packet;
		byte[] body = messagePacket.getBody();
		byte type = messagePacket.getType();
		if (body == null) {
			logger.debug("Invalid message, client: {}, drop it.", channelContext.getClientNode());
			return;
		}

		switch (type) {
			case MessagePacketType.RES_NEW_MESSAGE -> handler.newMessage(body);
			case MessagePacketType.RES_BLOCK_SYNC -> handler.syncBlock(body);
			case MessagePacketType.RES_NEW_BLOCK -> handler.newBlock(body);
		}
	}

	public Packet heartbeatPacket(ChannelContext context)
	{
		// disable heartbeat from tio framework
		return null;
	}
}
