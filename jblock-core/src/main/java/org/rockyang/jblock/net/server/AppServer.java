package org.rockyang.jblock.net.server;

import org.rockyang.jblock.net.conf.AppConfig;
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
	private final AppConfig appConfig;

	public AppServer(AppConfig appConfig, AppServerHandler serverHandler, AppServerListener serverListener)
	{
		TioServerConfig serverConfig = new TioServerConfig(AppConfig.SERVER_GROUP_NAME, serverHandler, serverListener);
		serverConfig.setHeartbeatTimeout(AppConfig.HEART_TIMEOUT);
		this.serverConfig = serverConfig;
		this.appConfig = appConfig;
	}

	@PostConstruct
	public void start() throws IOException
	{
		TioServer server = new TioServer(serverConfig);
		server.start(appConfig.getServerAddress(), appConfig.getServerPort());
	}
}