package org.rockyang.jblock.net.client;

import org.rockyang.jblock.chain.event.NewPeerEvent;
import org.rockyang.jblock.net.ApplicationContextProvider;
import org.rockyang.jblock.net.base.Peer;
import org.rockyang.jblock.net.conf.TioConfig;
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
public class AppClientListener implements TioClientListener {

	private static final Logger logger = LoggerFactory.getLogger(AppClientListener.class);

	@Override
	public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect)
	{
		if (isConnected) {
			logger.info("New node connected: {}", channelContext.getServerNode());
			Peer peer = new Peer(channelContext.getServerNode().getIp(), channelContext.getServerNode().getPort());
			// fire the new node connected event
			ApplicationContextProvider.publishEvent(new NewPeerEvent(peer));
			// bind peer to group
			Tio.bindGroup(channelContext, TioConfig.CLIENT_GROUP_NAME);
		} else {
			logger.warn("New node connected failed: {}", channelContext.getServerNode());
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

	}
}
