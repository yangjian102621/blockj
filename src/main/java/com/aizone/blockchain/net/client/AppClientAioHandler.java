package com.aizone.blockchain.net.client;

import com.aizone.blockchain.net.base.BaseAioHandler;
import com.aizone.blockchain.net.base.MessagePacket;
import com.aizone.blockchain.net.base.MessagePacketType;
import com.aizone.blockchain.utils.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;

/**
 * 客户端 AioHandler 实现
 * @author yangjian
 */
@Component
public class AppClientAioHandler extends BaseAioHandler implements ClientAioHandler {

	private static Logger logger = LoggerFactory.getLogger(AppClientAioHandler.class);

	/**
	 * 心跳包
	 */
	private static MessagePacket heartbeatPacket = new MessagePacket(MessagePacketType.STRING_MESSAGE);

	/**
	 * 处理消息
	 */
	@Override
	public void handler(Packet packet, ChannelContext channelContext) throws Exception {

		MessagePacket messagePacket = (MessagePacket) packet;
		byte[] body = messagePacket.getBody();
		byte type = messagePacket.getType();
		if (body != null) {
			switch (type) {
				case MessagePacketType.STRING_MESSAGE:
					String str = (String) SerializeUtils.unSerialize(body);
					logger.info("收到消息："+str);
					break;

				case MessagePacketType.RES_CONFRIM_TRANSACTION:
					logger.info("收到交易确认响应");
					break;

			}

		}

		return;
	}

	/**
	 * 此方法如果返回null，框架层面则不会发心跳；如果返回非null，框架层面会定时发本方法返回的消息包
	 * @return
	 */
	@Override
	public MessagePacket heartbeatPacket() {
		return heartbeatPacket;
	}
}
