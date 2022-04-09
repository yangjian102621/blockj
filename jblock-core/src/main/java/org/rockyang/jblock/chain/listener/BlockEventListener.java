package org.rockyang.jblock.chain.listener;

import org.rockyang.jblock.chain.Block;
import org.rockyang.jblock.chain.event.NewBlockEvent;
import org.rockyang.jblock.chain.event.PeerConnectEvent;
import org.rockyang.jblock.chain.event.SyncBlockEvent;
import org.rockyang.jblock.chain.service.BlockService;
import org.rockyang.jblock.net.base.MessagePacket;
import org.rockyang.jblock.net.base.MessagePacketType;
import org.rockyang.jblock.net.client.AppClient;
import org.rockyang.jblock.utils.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author yangjian
 */
@Component
public class BlockEventListener {

	private static final Logger logger = LoggerFactory.getLogger(BlockEventListener.class);

	private AppClient client;
	private BlockService blockService;

	public BlockEventListener(AppClient appClient, BlockService blockService)
	{
		this.client = appClient;
		this.blockService = blockService;
	}

	// mine a new block event
	@EventListener(NewBlockEvent.class)
	public void newBlock(NewBlockEvent event)
	{

		logger.info("++++++++++++++ 开始广播新区块 +++++++++++++++++++++");
		Block block = (Block) event.getSource();
		MessagePacket messagePacket = new MessagePacket();
		messagePacket.setType(MessagePacketType.REQ_NEW_BLOCK);
		messagePacket.setBody(SerializeUtils.serialize(block));
		client.sendGroup(messagePacket);
	}

	// start sync blocks when a new node is connected
	@EventListener(PeerConnectEvent.class)
	public void nodeConnected()
	{
		// get the chain head
		long head = blockService.chainHead();
		syncBlock(new SyncBlockEvent(head + 1));
	}

	@EventListener(ApplicationReadyEvent.class)
	public void appReady(ApplicationReadyEvent event)
	{
	}

	// sync the specified height block
	@EventListener(SyncBlockEvent.class)
	public void syncBlock(SyncBlockEvent event)
	{
		logger.info("++++++++++++++++++++++++++++++ start to sync block {} +++++++++++++++++++++++++++++++++", event.getSource());
		long height = (long) event.getSource();
		if (height == 0) {
			long head = blockService.chainHead();
			if (head > 0) {
				height = head;
			}
		}
		MessagePacket messagePacket = new MessagePacket();
		messagePacket.setType(MessagePacketType.REQ_BLOCK_SYNC);
		messagePacket.setBody(SerializeUtils.serialize(height + 1));
		// @TODO: maybe we should not to send all peers to fetch block
		// it's a waste of network, we can choose some well-synchronized nodes ONLY.
		client.sendGroup(messagePacket);
	}


}
