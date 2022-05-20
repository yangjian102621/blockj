package org.rockyang.blockj.chain.listener;

import org.rockyang.blockj.base.model.Block;
import org.rockyang.blockj.base.utils.SerializeUtils;
import org.rockyang.blockj.chain.event.NewBlockEvent;
import org.rockyang.blockj.chain.event.SyncBlockEvent;
import org.rockyang.blockj.net.base.MessagePacket;
import org.rockyang.blockj.net.base.MessagePacketType;
import org.rockyang.blockj.net.client.P2pClient;
import org.rockyang.blockj.service.BlockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author yangjian
 */
@Component
public class BlockEventListener {

	private static final Logger logger = LoggerFactory.getLogger(BlockEventListener.class);

	private final P2pClient client;
	private final BlockService blockService;

	public BlockEventListener(P2pClient p2pClient, BlockService blockService)
	{
		this.client = p2pClient;
		this.blockService = blockService;
	}

//	@EventListener(ApplicationReadyEvent.class)
//	public void appReady()
//	{
//		ApplicationContextProvider.publishEvent(new SyncBlockEvent(0));
//	}

	// mine a new block event
	@EventListener(NewBlockEvent.class)

	public void newBlock(NewBlockEvent event)
	{
		Block block = (Block) event.getSource();
		logger.info("NewBlockEvent: start to broadcast block {}", block.getHeader().getHeight());
		MessagePacket messagePacket = new MessagePacket();
		messagePacket.setType(MessagePacketType.REQ_NEW_BLOCK);
		messagePacket.setBody(SerializeUtils.serialize(block));
		client.sendGroup(messagePacket);
	}

	// sync the specified height block
	@EventListener(SyncBlockEvent.class)
	public void syncBlock(SyncBlockEvent event)
	{
		long height = (long) event.getSource();
		if (height == 0) {
			long head = blockService.chainHead();
			if (head > 0) {
				height = head + 1;
			}
		}
		logger.info("SyncBlockEvent: start to sync block {}", height);
		MessagePacket messagePacket = new MessagePacket();
		messagePacket.setType(MessagePacketType.REQ_BLOCK_SYNC);
		messagePacket.setBody(SerializeUtils.serialize(height));
		// @TODO: maybe we should not to send all peers to fetch block
		// @TODO: it's a waste of network, we can choose some well-synchronized nodes ONLY.
		client.sendGroup(messagePacket);
	}
}
