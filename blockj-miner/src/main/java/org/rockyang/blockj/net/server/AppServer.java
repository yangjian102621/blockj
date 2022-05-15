package org.rockyang.blockj.net.server;

import org.rockyang.blockj.net.conf.NetConfig;
import org.springframework.stereotype.Component;
import org.tio.server.TioServer;
import org.tio.server.TioServerConfig;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Tio server starter
 *
 * @author yangjian
 */
@Component
public class AppServer {

	private final TioServerConfig serverConfig;
	private final NetConfig netConfig;

	public AppServer(NetConfig netConfig, AppServerHandler serverHandler, AppServerListener serverListener)
	{
		TioServerConfig serverConfig = new TioServerConfig(NetConfig.SERVER_NAME, serverHandler, serverListener);
		// disable heartbeat from tio framework
		serverConfig.setHeartbeatTimeout(0);
		this.serverConfig = serverConfig;
		this.netConfig = netConfig;
	}

	@PostConstruct
	public void start() throws IOException
	{
		TioServer server = new TioServer(serverConfig);
		server.start(netConfig.getServerAddress(), netConfig.getServerPort());
	}
}