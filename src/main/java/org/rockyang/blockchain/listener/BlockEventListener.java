package org.rockyang.blockchain.listener;

import com.google.common.base.Optional;
import org.rockyang.blockchain.core.Block;
import org.rockyang.blockchain.db.DBAccess;
import org.rockyang.blockchain.event.FetchNextBlockEvent;
import org.rockyang.blockchain.event.MineBlockEvent;
import org.rockyang.blockchain.net.base.MessagePacket;
import org.rockyang.blockchain.net.base.MessagePacketType;
import org.rockyang.blockchain.net.client.AppClient;
import org.rockyang.blockchain.utils.SerializeUtils;
import org.rockyang.blockchain.core.Block;
import org.rockyang.blockchain.db.DBAccess;
import org.rockyang.blockchain.event.FetchNextBlockEvent;
import org.rockyang.blockchain.event.MineBlockEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 区块事件监听器
 * @author yangjian
 * @since 18-4-19
 */
@Component
public class BlockEventListener {

	@Autowired
	private AppClient appClient;
	@Autowired
	private DBAccess dbAccess;
	private static Logger logger = LoggerFactory.getLogger(BlockEventListener.class);

	/**
	 * 挖矿事件监听
	 * @param event
	 */
	@EventListener(MineBlockEvent.class)
	public void mineBlock(MineBlockEvent event) {

		logger.info("++++++++++++++ 开始广播新区块 +++++++++++++++++++++");
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

		logger.info("++++++++++++++++++++++++++++++ 开始群发信息获取 next Block +++++++++++++++++++++++++++++++++");
		Integer blockIndex = (Integer) event.getSource();
		if (blockIndex == 0) {
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
