package org.rockyang.jblock.chain.listener;

import org.rockyang.jblock.base.utils.SerializeUtils;
import org.rockyang.jblock.chain.event.NewPeerEvent;
import org.rockyang.jblock.net.base.MessagePacket;
import org.rockyang.jblock.net.base.MessagePacketType;
import org.rockyang.jblock.net.base.Peer;
import org.rockyang.jblock.net.client.AppClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author yangjian
 */
@Component
public class PeerEventListener {
	private static final Logger logger = LoggerFactory.getLogger(PeerEventListener.class);

	private final AppClient client;

	public PeerEventListener(AppClient client)
	{
		this.client = client;
	}

	// when a new peer connected, notify all peers to connect the new peer
	@EventListener(NewPeerEvent.class)
	public void newPeerConnected(NewPeerEvent event)
	{
		Peer peer = (Peer) event.getSource();
		logger.info("NewPeerEvent: new peer connected {}", peer);
		MessagePacket packet = new MessagePacket();
		packet.setType(MessagePacketType.REQ_NEW_PEER);
		packet.setBody(SerializeUtils.serialize(peer));
		// broadcast peer
		client.sendGroup(packet);
	}
}
