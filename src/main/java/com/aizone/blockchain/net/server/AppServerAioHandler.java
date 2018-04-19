package com.aizone.blockchain.net.server;

import com.aizone.blockchain.net.base.BaseAioHandler;
import com.aizone.blockchain.net.base.MessagePacket;
import com.aizone.blockchain.utils.SerializeUtils;
import org.springframework.stereotype.Component;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioHandler;

/**
 * 服务端 AioHandler 实现
 * @author yangjian
 */
@Component
public class AppServerAioHandler extends BaseAioHandler implements ServerAioHandler {

	/**
	 * 处理消息
	 */
	@Override
	public void handler(Packet packet, ChannelContext channelContext) throws Exception {

		MessagePacket messagePacket = (MessagePacket) packet;
		byte[] body = messagePacket.getBody();
		if (body != null) {
			String str = (String) SerializeUtils.unSerialize(body);
			System.out.println("收到消息：" + str);

			MessagePacket resPacket = new MessagePacket();
			resPacket.setBody(SerializeUtils.serialize("收到了你的消息，你的消息是:" + str));
			Aio.send(channelContext, resPacket);
		}
		return;
	}
}
