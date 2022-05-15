package org.rockyang.blockj.chain.listener;

import org.rockyang.blockj.base.model.Message;
import org.rockyang.blockj.base.utils.SerializeUtils;
import org.rockyang.blockj.chain.event.NewMessageEvent;
import org.rockyang.blockj.net.base.MessagePacket;
import org.rockyang.blockj.net.base.MessagePacketType;
import org.rockyang.blockj.net.client.AppClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author yangjian
 */
@Component
public class MessageEventListener {

	private final AppClient client;
	private static final Logger logger = LoggerFactory.getLogger(MessageEventListener.class);

	public MessageEventListener(AppClient client)
	{
		this.client = client;
	}

	@EventListener(NewMessageEvent.class)
	public void broadCastMessage(NewMessageEvent event)
	{
		Message message = (Message) event.getSource();
		logger.info("NewMessageEvent: start to broadcast new message, cid: {}", message.getCid());
		MessagePacket messagePacket = new MessagePacket();
		messagePacket.setType(MessagePacketType.REQ_NEW_MESSAGE);
		messagePacket.setBody(SerializeUtils.serialize(message));
		client.sendGroup(messagePacket);
	}

}
