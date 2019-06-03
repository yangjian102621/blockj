package org.rockyang.blockchain.listener;

import org.rockyang.blockchain.core.Transaction;
import org.rockyang.blockchain.event.SendTransactionEvent;
import org.rockyang.blockchain.net.base.MessagePacket;
import org.rockyang.blockchain.net.base.MessagePacketType;
import org.rockyang.blockchain.net.client.AppClient;
import org.rockyang.blockchain.utils.SerializeUtils;
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
