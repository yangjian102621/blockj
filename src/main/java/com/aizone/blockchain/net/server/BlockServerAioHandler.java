package com.aizone.blockchain.net.server;

import com.aizone.blockchain.net.base.BaseAioHandler;
import com.aizone.blockchain.net.base.BlockPacket;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioHandler;

/**
 * 区块同步服务端 AioHandler 实现
 * @author yangjian
 */
public class BlockServerAioHandler extends BaseAioHandler implements ServerAioHandler {

	/**
	 * 处理消息
	 */
	@Override
	public void handler(Packet packet, ChannelContext channelContext) throws Exception {

		BlockPacket helloPacket = (BlockPacket) packet;
		byte[] body = helloPacket.getBody();
		if (body != null) {
			String str = new String(body, BlockPacket.CHARSET);
			System.out.println("收到消息：" + str);

			BlockPacket resPacket = new BlockPacket();
			resPacket.setBody(("收到了你的消息，你的消息是:" + str).getBytes(BlockPacket.CHARSET));
			Aio.send(channelContext, resPacket);
		}
		return;
	}
}
