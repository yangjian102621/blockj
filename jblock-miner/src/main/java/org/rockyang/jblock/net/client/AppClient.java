package org.rockyang.jblock.net.client;

import org.apache.commons.codec.binary.StringUtils;
import org.rockyang.jblock.base.model.Peer;
import org.rockyang.jblock.base.utils.SerializeUtils;
import org.rockyang.jblock.net.base.MessagePacket;
import org.rockyang.jblock.net.base.MessagePacketType;
import org.rockyang.jblock.net.conf.NetConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tio.client.ClientChannelContext;
import org.tio.client.ReconnConf;
import org.tio.client.TioClient;
import org.tio.client.TioClientConfig;
import org.tio.core.Node;
import org.tio.core.Tio;

import javax.annotation.PostConstruct;

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

	public AppClient(NetConfig netConfig, AppClientHandler clientHandler, AppClientListener clientListener)
	{
		// set the auto reconnect
		ReconnConf reconnConf = new ReconnConf(5000L, 20);
		// init client config
		TioClientConfig clientConfig = new TioClientConfig(clientHandler, clientListener, reconnConf);
		// disable heartbeat from tio framework
		clientConfig.setHeartbeatTimeout(0);
		this.clientConfig = clientConfig;
		this.netConfig = netConfig;
	}

	@PostConstruct
	public void run() throws Exception
	{
		this.client = new TioClient(clientConfig);
		// try to connect the genesis node
		connect(new Node(netConfig.getGenesisAddress(), netConfig.getGenesisPort()));
	}

	public void sendGroup(MessagePacket messagePacket)
	{
		if (NetConfig.SERVERS.size() > 0) {
			Tio.sendToGroup(clientConfig, NetConfig.NODE_GROUP_NAME, messagePacket);
		}
	}

	// connect a new node
	public boolean connect(Node node) throws Exception
	{
		if (StringUtils.equals(node.getIp(), netConfig.getServerAddress()) && node.getPort() == netConfig.getServerPort()) {
			logger.info("skip self connections, {}", node.toString());
			return false;
		}

		if (NetConfig.SERVERS.containsKey(node)) {
			return false;
		}

		NetConfig.SERVERS.put(node, true);
		ClientChannelContext channelContext = client.connect(node);

		// send self server connection info
		Peer server = new Peer(netConfig.getServerAddress(), netConfig.getServerPort());
		MessagePacket packet = new MessagePacket();
		packet.setType(MessagePacketType.REQ_NEW_PEER);
		packet.setBody(SerializeUtils.serialize(server));
		Tio.send(channelContext, packet);
		return true;
	}

}
