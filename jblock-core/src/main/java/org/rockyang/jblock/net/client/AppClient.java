package org.rockyang.jblock.net.client;

import org.apache.commons.lang3.StringUtils;
import org.rockyang.jblock.net.base.MessagePacket;
import org.rockyang.jblock.net.base.MessagePacketType;
import org.rockyang.jblock.net.base.Peer;
import org.rockyang.jblock.net.conf.AppConfig;
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
 *
 * @author yangjian
 */
@Component
public class AppClient {

	private static final Logger logger = LoggerFactory.getLogger(AppClient.class);
	private final TioClientConfig clientConfig;
	private final TioClient client;
	private final AppConfig appConfig;


	public AppClient(TioClientConfig clientConfig, AppConfig appConfig) throws Exception
	{
		this.clientConfig = clientConfig;
		this.client = new TioClient(clientConfig);
		this.appConfig = appConfig;
		// try to connect the genesis node
		connect(new Peer(appConfig.getGenesisAddress(), appConfig.getGenesisPort()));
	}

	public void sendGroup(MessagePacket messagePacket)
	{
		Tio.sendToGroup(clientConfig, TioConfig.CLIENT_GROUP_NAME, messagePacket);
	}

	// connect a new node
	public void connect(Peer peer) throws Exception
	{
		if (StringUtils.equals(peer.getIp(), appConfig.getServerAddress()) && peer.getPort() == appConfig.getServerPort()) {
			logger.info("skip self connections, {}", peer.toString());
			return;
		}
		ClientChannelContext channelContext = client.connect(peer);
		// send a hello message to server
		Peer server = new Peer(appConfig.getServerAddress(), appConfig.getServerPort());
		MessagePacket packet = new MessagePacket();
		packet.setType(MessagePacketType.HELLO_MESSAGE);
		packet.setBody(SerializeUtils.serialize(MessagePacket.HELLO_MESSAGE));
		if (Tio.send(channelContext, packet)) {
			Tio.bindGroup(channelContext, TioConfig.CLIENT_GROUP_NAME);
		}
	}
}
