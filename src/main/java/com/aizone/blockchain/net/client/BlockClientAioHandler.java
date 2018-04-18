package com.aizone.blockchain.net.client;

import com.aizone.blockchain.net.base.BaseAioHandler;
import com.aizone.blockchain.net.base.BlockPacket;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;

/**
 * 区块同步客户端 AioHandler 实现
 * @author yangjian
 */
public class BlockClientAioHandler extends BaseAioHandler implements ClientAioHandler {

	/**
	 * 心跳包
	 */
	private static BlockPacket heartbeatPacket = new BlockPacket();

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
		}

		return;
	}

	/**
	 * 此方法如果返回null，框架层面则不会发心跳；如果返回非null，框架层面会定时发本方法返回的消息包
	 * @return
	 */
	@Override
	public BlockPacket heartbeatPacket() {
		return heartbeatPacket;
	}
}
