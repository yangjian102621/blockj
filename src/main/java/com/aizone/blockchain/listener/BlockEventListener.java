package com.aizone.blockchain.listener;

import com.aizone.blockchain.core.Block;
import com.aizone.blockchain.event.MineBlockEvent;
import com.aizone.blockchain.event.SyncBlockEvent;
import com.aizone.blockchain.net.base.MessagePacket;
import com.aizone.blockchain.net.base.MessagePacketType;
import com.aizone.blockchain.net.client.AppClient;
import com.aizone.blockchain.utils.SerializeUtils;
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
	 * @param blockIndex
	 */
	@EventListener(SyncBlockEvent.class)
	public void syncBlock(Integer blockIndex) {

	}
}
