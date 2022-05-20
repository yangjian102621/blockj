package org.rockyang.blockj.net.client;

import org.rockyang.blockj.net.conf.NetConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tio.client.intf.TioClientListener;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.core.intf.Packet;

/**
 * @author yangjian
 */
@Component
public class P2pClientListener implements TioClientListener {

	private static final Logger logger = LoggerFactory.getLogger(P2pClientListener.class);

	@Override
	public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect)
	{
		if (isConnected) {
			logger.info("Connect server {} successfully", channelContext.getServerNode());
			// bind peer to group
			Tio.bindGroup(channelContext, NetConfig.NODE_GROUP_NAME);
		}
	}

	@Override
	public void onAfterDecoded(ChannelContext channelContext, Packet packet, int i)
	{

	}

	@Override
	public void onAfterReceivedBytes(ChannelContext channelContext, int i)
	{

	}


	@Override
	public void onAfterSent(ChannelContext channelContext, Packet packet, boolean b)
	{

	}

	@Override
	public void onAfterHandled(ChannelContext channelContext, Packet packet, long l)
	{

	}

	@Override
	public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String s, boolean b)
	{
		Tio.unbindGroup(NetConfig.NODE_GROUP_NAME, channelContext);
	}
}
