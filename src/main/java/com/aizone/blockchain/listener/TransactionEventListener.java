package com.aizone.blockchain.listener;

import com.aizone.blockchain.core.Transaction;
import com.aizone.blockchain.event.SendTransactionEvent;
import com.aizone.blockchain.net.base.MessagePacket;
import com.aizone.blockchain.net.base.MessagePacketType;
import com.aizone.blockchain.net.client.AppClient;
import com.aizone.blockchain.utils.SerializeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 发送交易事件监听器
 * @author yangjian
 * @since 18-4-19
 */
@Component
public class TransactionEventListener {

	@Autowired
	private AppClient appClient;

	/**
	 * 向所有客户端广播交易
	 * @param event
	 */
	@EventListener(SendTransactionEvent.class)
	public void sendTransaction(SendTransactionEvent event) {

		Transaction transaction = (Transaction) event.getSource();
		MessagePacket messagePacket = new MessagePacket();
		messagePacket.setType(MessagePacketType.REQ_CONFIRM_TRANSACTION);
		messagePacket.setBody(SerializeUtils.serialize(transaction));
		appClient.sendGroup(messagePacket);
	}

}
