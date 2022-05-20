package org.rockyang.blockj.net.server;

import org.rockyang.blockj.chain.event.SyncBlockEvent;
import org.rockyang.blockj.conf.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.server.intf.TioServerListener;

/**
 * @author yangjian
 */
@Component
public class P2pServerListener implements TioServerListener {

	private static final Logger logger = LoggerFactory.getLogger(P2pServerListener.class);

	@Override
	public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect)
	{
		if (isConnected) {
			logger.info("New client connected: {}", channelContext.getClientNode());
			// start to sync block
			ApplicationContextProvider.publishEvent(new SyncBlockEvent(0));
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
	public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess)
	{

	}

	@Override
	public void onAfterHandled(ChannelContext channelContext, Packet packet, long l)
	{

	}

	@Override
	public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove)
	{

	}

	@Override
	public boolean onHeartbeatTimeout(ChannelContext channelContext, Long aLong, int i)
	{
		return true;
	}
}
