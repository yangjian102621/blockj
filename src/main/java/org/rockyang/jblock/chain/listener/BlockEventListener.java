package org.rockyang.jblock.chain.listener;

import org.rockyang.jblock.chain.Block;
import org.rockyang.jblock.db.Datastore;
import org.rockyang.jblock.chain.event.BlockConfirmNumEvent;
import org.rockyang.jblock.chain.event.FetchNextBlockEvent;
import org.rockyang.jblock.chain.event.NewBlockEvent;
import org.rockyang.jblock.net.ApplicationContextProvider;
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
	private Datastore datastore;

	public BlockEventListener(AppClient appClient, Datastore datastore)
	{
		this.appClient = appClient;
		this.datastore = datastore;
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

	/**
	 * 向所有连接的节点发起同步区块请求
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void fetchNextBlock()
	{
		ApplicationContextProvider.publishEvent(new FetchNextBlockEvent(0));
	}

	/**
	 * 同步下一个区块
	 * @param event
	 */
	@EventListener(FetchNextBlockEvent.class)
	public void fetchNextBlock(FetchNextBlockEvent event)
	{

//		logger.info("++++++++++++++++++++++++++++++ 开始群发信息获取 next Block +++++++++++++++++++++++++++++++++");
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

	@EventListener(BlockConfirmNumEvent.class)
	public void IncBlockConfirmNum(BlockConfirmNumEvent event)
	{
		logger.info("++++++++++++++ 增加区块确认数 ++++++++++++++++++");
		Integer blockIndex = (Integer) event.getSource();
		MessagePacket messagePacket = new MessagePacket();
		messagePacket.setType(MessagePacketType.REQ_INC_CONFIRM_NUM);
		messagePacket.setBody(SerializeUtils.serialize(blockIndex));
		//群发消息
		appClient.sendGroup(messagePacket);
	}

}
