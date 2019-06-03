package org.rockyang.blockchain.net.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioListener;

/**
 * 服务端连接状态的监听器
 * @author yangjian
 */
@Component
public class AppServerAioListener implements ServerAioListener {

	private static Logger log = LoggerFactory.getLogger(AppServerAioListener.class);

	@Override
	public void onAfterClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) {

	}

	@Override
	public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) {

	}

	@Override
	public void onAfterReceived(ChannelContext channelContext, Packet packet, int packetSize) {

	}

	@Override
	public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess) {

	}

	@Override
	public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) {

	}
}
