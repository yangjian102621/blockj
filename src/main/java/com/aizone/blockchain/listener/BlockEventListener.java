package com.aizone.blockchain.listener;

import com.aizone.blockchain.core.Block;
import com.aizone.blockchain.db.DBAccess;
import com.aizone.blockchain.event.FetchNextBlockEvent;
import com.aizone.blockchain.event.MineBlockEvent;
import com.aizone.blockchain.net.base.MessagePacket;
import com.aizone.blockchain.net.base.MessagePacketType;
import com.aizone.blockchain.net.client.AppClient;
import com.aizone.blockchain.net.conf.TioProperties;
import com.aizone.blockchain.utils.SerializeUtils;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.tio.client.ClientGroupContext;

import javax.annotation.Resource;

/**
 * 区块事件监听器
 * @author yangjian
 * @since 18-4-19
 */
@Component
public class BlockEventListener {

	@Resource
	private ClientGroupContext clientGroupContext;
	@Autowired
	private AppClient appClient;
	@Autowired
	private TioProperties tioProperties;
	@Autowired
	private DBAccess dbAccess;

	private static Logger logger = LoggerFactory.getLogger(AppClient.class);

	/**
	 * 挖矿事件监听
	 * @param event
	 */
	@EventListener(MineBlockEvent.class)
	public void mineBlock(MineBlockEvent event) {

		Block block = (Block) event.getSource();
		MessagePacket messagePacket = new MessagePacket();
		messagePacket.setType(MessagePacketType.REQ_NEW_BLOCK);
		messagePacket.setBody(SerializeUtils.serialize(block));
		appClient.sendGroup(messagePacket);
	}

	/**
	 * 同步下一个区块
	 * @param event
	 */
	@EventListener(FetchNextBlockEvent.class)
	public void fetchNextBlock(FetchNextBlockEvent event) {

		logger.info("开始群发信息获取next Block");
		Integer blockIndex = 0;
		if (null != event) {
			blockIndex = (Integer) event.getSource();
		} else {
			Optional<Object> lastBlockIndex = dbAccess.getLastBlockIndex();
			if (lastBlockIndex.isPresent()) {
				blockIndex = (Integer) lastBlockIndex.get();
			}
		}
		MessagePacket messagePacket = new MessagePacket();
		messagePacket.setType(MessagePacketType.REQ_SYNC_NEXT_BLOCK);
		messagePacket.setBody(SerializeUtils.serialize(blockIndex+1));
		//群发消息，从群组节点去获取下一个区块
		appClient.sendGroup(messagePacket);
	}

}
