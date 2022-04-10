package org.rockyang.jblock.chain.listener;

import org.rockyang.jblock.chain.event.NewPeerEvent;
import org.rockyang.jblock.chain.service.PeerService;
import org.rockyang.jblock.net.base.MessagePacket;
import org.rockyang.jblock.net.base.MessagePacketType;
import org.rockyang.jblock.net.base.Peer;
import org.rockyang.jblock.net.client.AppClient;
import org.rockyang.jblock.utils.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;

/**
 * @author yangjian
 */
public class PeerEventListener {
	private static final Logger logger = LoggerFactory.getLogger(PeerEventListener.class);

	private final PeerService peerService;
	private final AppClient client;

	public PeerEventListener(AppClient client, PeerService peerService)
	{
		this.client = client;
		this.peerService = peerService;
	}

	// when a new peer connected, notify all peers to connect the new peer
	@EventListener(NewPeerEvent.class)
	public void peerConnected(NewPeerEvent event)
	{
		logger.info("++++++++++++++++++++++++++ new peer connected +++++++++++++++++++++++++++");
		Peer peer = (Peer) event.getSource();
		MessagePacket packet = new MessagePacket();
		packet.setType(MessagePacketType.REQ_BLOCK_SYNC);
		packet.setBody(SerializeUtils.serialize(peer));

		// save peer info
		peerService.addPeer(peer);

		// broadcast peer
		client.sendGroup(packet);
	}
}
