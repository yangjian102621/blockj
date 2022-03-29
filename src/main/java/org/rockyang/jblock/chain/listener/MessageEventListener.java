package org.rockyang.jblock.chain.listener;

import org.rockyang.jblock.chain.Message;
import org.rockyang.jblock.chain.event.NewMessageEvent;
import org.rockyang.jblock.net.base.MessagePacket;
import org.rockyang.jblock.net.base.MessagePacketType;
import org.rockyang.jblock.net.client.AppClient;
import org.rockyang.jblock.utils.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 发送交易事件监听器
 * @author yangjian
 * @since 18-4-19
 */
@Component
public class MessageEventListener {

	@Autowired
	private AppClient appClient;
	private static Logger logger = LoggerFactory.getLogger(MessageEventListener.class);

	/**
	 * 向所有客户端广播交易
	 * @param event
	 */
	@EventListener(NewMessageEvent.class)
	public void sendTransaction(NewMessageEvent event)
	{

		logger.info("++++++++++++++ 开始广播新新的交易订单 +++++++++++++++++++++");
		Message message = (Message) event.getSource();
		MessagePacket messagePacket = new MessagePacket();
		messagePacket.setType(MessagePacketType.REQ_CONFIRM_TRANSACTION);
		messagePacket.setBody(SerializeUtils.serialize(message));
		appClient.sendGroup(messagePacket);
	}

}
