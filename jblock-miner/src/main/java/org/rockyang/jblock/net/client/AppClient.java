package org.rockyang.jblock.net.client;

import org.apache.commons.codec.binary.StringUtils;
import org.rockyang.jblock.base.utils.SerializeUtils;
import org.rockyang.jblock.net.base.MessagePacket;
import org.rockyang.jblock.net.base.MessagePacketType;
import org.rockyang.jblock.net.base.Peer;
import org.rockyang.jblock.net.conf.NetConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tio.client.ClientChannelContext;
import org.tio.client.ReconnConf;
import org.tio.client.TioClient;
import org.tio.client.TioClientConfig;
import org.tio.core.Tio;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tio client starter
 *
 * @author yangjian
 */
@Component
public class AppClient {

	private static final Logger logger = LoggerFactory.getLogger(AppClient.class);

	private TioClient client;
	private final NetConfig netConfig;
	private final TioClientConfig clientConfig;

	private static final Map<Peer, Boolean> connectedPeers = new ConcurrentHashMap<>(16);

	public AppClient(NetConfig netConfig, AppClientHandler clientHandler, AppClientListener clientListener)
	{
		// set the auto reconnect
		ReconnConf reconnConf = new ReconnConf(5000L, 20);
		// init client config
		TioClientConfig clientConfig = new TioClientConfig(clientHandler, clientListener, reconnConf);
		clientConfig.setHeartbeatTimeout(NetConfig.HEART_TIMEOUT);
		this.clientConfig = clientConfig;
		this.netConfig = netConfig;
	}

	@PostConstruct
	public void run()
	{
		new Thread(() -> {
			try {
				this.client = new TioClient(clientConfig);
				// try to connect the genesis node
				connect(new Peer(netConfig.getGenesisAddress(), netConfig.getGenesisPort()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	public void sendGroup(MessagePacket messagePacket)
	{
		Tio.sendToGroup(clientConfig, NetConfig.CLIENT_GROUP_NAME, messagePacket);
	}

	// connect a new node
	public boolean connect(Peer peer) throws Exception
	{
		if (StringUtils.equals(peer.getIp(), netConfig.getServerAddress()) && peer.getPort() == netConfig.getServerPort()) {
			logger.info("skip self connections, {}", peer.toString());
			return false;
		}
		if (connectedPeers.containsKey(peer)) {
			return false;
		}

		logger.info("try to connect peer {}", peer);
		ClientChannelContext channelContext = client.connect(peer);
		connectedPeers.put(peer, true);
		// send self server connection info
		Peer server = new Peer(netConfig.getServerAddress(), netConfig.getServerPort());
		MessagePacket packet = new MessagePacket();
		packet.setType(MessagePacketType.REQ_NEW_PEER);
		packet.setBody(SerializeUtils.serialize(server));
		if (Tio.send(channelContext, packet)) {
			Tio.bindGroup(channelContext, NetConfig.CLIENT_GROUP_NAME);
		}
		return true;
	}

}
