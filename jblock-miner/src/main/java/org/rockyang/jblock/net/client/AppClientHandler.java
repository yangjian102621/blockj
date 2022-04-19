package org.rockyang.jblock.net.client;

import org.rockyang.jblock.chain.sync.ClientHandler;
import org.rockyang.jblock.net.base.BaseHandler;
import org.rockyang.jblock.net.base.MessagePacket;
import org.rockyang.jblock.net.base.MessagePacketType;
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

	public AppClientHandler(ClientHandler handler)
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
		if (body != null) {
			switch (type) {
				case MessagePacketType.RES_NEW_MESSAGE -> handler.newMessage(body);
				case MessagePacketType.RES_BLOCK_SYNC -> handler.syncBlock(body);
				case MessagePacketType.RES_NEW_BLOCK -> handler.newBlock(body);
			} //end of switch
		}
	}

	public Packet heartbeatPacket(ChannelContext context)
	{
		// disable heartbeat from tio framework
		return null;
	}
}