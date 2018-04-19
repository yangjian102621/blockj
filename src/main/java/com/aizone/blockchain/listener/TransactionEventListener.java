package com.aizone.blockchain.listener;

import com.aizone.blockchain.event.SendTransactionEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 发送交易事件监听器
 * @author yangjian
 * @since 18-4-19
 */
@Component
public class TransactionEventListener {


	@EventListener(SendTransactionEvent.class)
	public void sendTransaction(SendTransactionEvent event) {
		//向所有客户端广播交易
	}

}
