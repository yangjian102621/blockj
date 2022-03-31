package org.rockyang.jblock.net.client;

import org.rockyang.jblock.store.Datastore;
import org.rockyang.jblock.net.base.MessagePacket;
import org.rockyang.jblock.net.base.Peer;
import org.rockyang.jblock.net.conf.TioConfig;
import org.rockyang.jblock.utils.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tio.client.ClientChannelContext;
import org.tio.client.TioClient;
import org.tio.client.TioClientConfig;
import org.tio.core.Tio;

/**
 * Tio client starter
 * @author yangjian
 *
 */
@Component
public class AppClient {

	private static final Logger logger = LoggerFactory.getLogger(AppClient.class);
	private TioClientConfig clientConfig;
	private TioClient client;
	private Datastore datastore;

	public AppClient(TioClientConfig clientConfig, Datastore datastore) throws Exception
	{
		this.clientConfig = clientConfig;
		this.client = new TioClient(clientConfig);
		this.datastore = datastore;
		// @TODO connect the genesis node?
	}

	public void sendGroup(MessagePacket messagePacket)
	{
		Tio.sendToGroup(clientConfig, TioConfig.CLIENT_GROUP_NAME, messagePacket);
	}

	// connect a new node
	public void connect(Peer peer) throws Exception
	{
		ClientChannelContext channelContext = client.connect(peer);
		// send a hello message after connected
		MessagePacket packet = new MessagePacket(SerializeUtils.serialize(MessagePacket.HELLO_MESSAGE));
		if (Tio.send(channelContext, packet)) {
			Tio.bindGroup(channelContext, TioConfig.CLIENT_GROUP_NAME);
		}
	}
}
