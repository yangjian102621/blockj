package org.rockyang.jblock.chain.listener;

import org.rockyang.jblock.chain.Block;
import org.rockyang.jblock.chain.event.NewBlockEvent;
import org.rockyang.jblock.chain.event.SyncBlockEvent;
import org.rockyang.jblock.chain.service.ChainService;
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
 * 区块事件监听器
 * @author yangjian
 * @since 18-4-19
 */
@Component
public class BlockEventListener {

	private static final Logger logger = LoggerFactory.getLogger(BlockEventListener.class);

	private AppClient appClient;
	private ChainService chainService;

	public BlockEventListener(AppClient appClient, ChainService chainService)
	{
		this.appClient = appClient;
		this.chainService = chainService;
	}

	/**
	 * 挖矿事件监听
	 * @param event
	 */
	@EventListener(NewBlockEvent.class)
	public void newBlock(NewBlockEvent event) {

		logger.info("++++++++++++++ 开始广播新区块 +++++++++++++++++++++");
		Block block = (Block) event.getSource();
		MessagePacket messagePacket = new MessagePacket();
		messagePacket.setType(MessagePacketType.REQ_NEW_BLOCK);
		messagePacket.setBody(SerializeUtils.serialize(block));
		appClient.sendGroup(messagePacket);
	}

	// start sync blocks when the application is ready
	@EventListener(ApplicationReadyEvent.class)
	public void appReady()
	{
		// get the chain head
		Object o = chainService.chainHead();
		int head = 0;
		if (o != null) {
			head = Integer.parseInt(o.toString());
		}
		syncBlock(new SyncBlockEvent(head+1));
	}

	// sync the specified height block
	@EventListener(SyncBlockEvent.class)
	public void syncBlock(SyncBlockEvent event)
	{


		logger.info("++++++++++++++++++++++++++++++ start to sync block {} +++++++++++++++++++++++++++++++++", event.getSource());
//		Integer blockIndex = (Integer) event.getSource();
//		if (blockIndex == 0) {
//			Optional<Object> lastBlockIndex = dataStore.getLastBlockIndex();
//			if (lastBlockIndex.isPresent()) {
//				blockIndex = (Integer) lastBlockIndex.get();
//			}
//		}
//		MessagePacket messagePacket = new MessagePacket();
//		messagePacket.setType(MessagePacketType.REQ_SYNC_NEXT_BLOCK);
//		messagePacket.setBody(SerializeUtils.serialize(blockIndex+1));
//		//群发消息，从群组节点去获取下一个区块
//		appClient.sendGroup(messagePacket);
	}


}
