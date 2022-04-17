package org.rockyang.jblock.net.server;

import org.rockyang.jblock.net.conf.NetConfig;
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
		TioServerConfig serverConfig = new TioServerConfig(NetConfig.SERVER_GROUP_NAME, serverHandler, serverListener);
		serverConfig.setHeartbeatTimeout(NetConfig.HEART_TIMEOUT);
		this.serverConfig = serverConfig;
		this.netConfig = netConfig;
	}

	@PostConstruct
	public void start()
	{
		new Thread(() -> {
			try {
				TioServer server = new TioServer(serverConfig);
				server.start(netConfig.getServerAddress(), netConfig.getServerPort());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();

	}
}