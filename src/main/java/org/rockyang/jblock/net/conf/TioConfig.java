package org.rockyang.jblock.net.conf;

import org.rockyang.jblock.net.client.AppClientHandler;
import org.rockyang.jblock.net.client.AppClientListener;
import org.rockyang.jblock.net.server.AppServerHandler;
import org.rockyang.jblock.net.server.AppServerListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tio.client.ReconnConf;
import org.tio.client.TioClientConfig;
import org.tio.server.TioServerConfig;

/**
 * Group context 配置
 * @author yangjian
 * @since 18-4-18
 */
@Configuration
public class TioConfig {

	public static final String SERVER_GROUP_NAME = "jblock-server";
	public static final String CLIENT_GROUP_NAME = "jblock-client";
	public static final int HEART_TIMEOUT = 5000;

	// massage handler for client
	AppClientHandler clientHandler;
	// client message listener
	AppClientListener clientListener;
	// message handler for server
	AppServerHandler serverHandler;
	// server message listener
	AppServerListener serverListener;

	public TioConfig(AppClientHandler clientHandler, AppClientListener clientListener, AppServerHandler serverHandler, AppServerListener serverListener)
	{
		this.clientHandler = clientHandler;
		this.clientListener = clientListener;
		this.serverHandler = serverHandler;
		this.serverListener = serverListener;
	}

	@Bean
	public TioClientConfig clientConfig()
	{
		// set the auto reconnect
		ReconnConf reconnConf = new ReconnConf(5000L, 20);
		TioClientConfig clientConfig = new TioClientConfig(clientHandler, clientListener, reconnConf);
		clientConfig.setHeartbeatTimeout(HEART_TIMEOUT);
		return clientConfig;
	}

	@Bean
	public TioServerConfig serverConfig()
	{
		TioServerConfig serverConfig = new TioServerConfig(SERVER_GROUP_NAME,serverHandler,serverListener);
		serverConfig.setHeartbeatTimeout(HEART_TIMEOUT);
		return serverConfig;
	}

}
