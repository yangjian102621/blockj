package com.aizone.blockchain.listener;

import com.aizone.blockchain.event.MineBlockEvent;
import com.aizone.blockchain.event.SyncBlockEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 区块事件监听器
 * @author yangjian
 * @since 18-4-19
 */
@Component
public class BlockEventListener {

	/**
	 * 挖矿
	 * @param event
	 */
	@EventListener(MineBlockEvent.class)
	public void mineBlock(MineBlockEvent event) {
		//向所有客户端广播区块
	}

	/**
	 * 同步下一个区块
	 * @param blockIndex
	 */
	@EventListener(SyncBlockEvent.class)
	public void syncBlock(Integer blockIndex) {

	}
}
