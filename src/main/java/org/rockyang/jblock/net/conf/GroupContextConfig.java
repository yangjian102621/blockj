package org.rockyang.jblock.net.conf;

import org.rockyang.jblock.net.client.AppClientAioHandler;
import org.rockyang.jblock.net.client.AppClientAioListener;
import org.rockyang.jblock.net.server.AppServerAioHandler;
import org.rockyang.jblock.net.server.AppServerAioListener;
import org.springframework.beans.factory.annotation.Autowired;
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
public class GroupContextConfig {

	@Autowired
	TioProps tioProps;

	/**
	 * 客户端消息 handler, 包括编码、解码、消息处理
	 */
	@Autowired
	AppClientAioHandler clientHandler;

	/**
	 * 客户端事件监听器
	 */
	@Autowired
	AppClientAioListener clientListener;

	/**
	 * 服务端消息 handler, 包括编码、解码、消息处理
	 */
	@Autowired
	AppServerAioHandler serverHandler;

	/**
	 * 服务端事件监听器
	 */
	@Autowired
	AppServerAioListener serverListener;

	/**
	 * 客户端一组连接共用的上下文对象
	 * @return
	 */
	@Bean
	public TioClientConfig clientGroupContext() {

		//断链后自动连接
		ReconnConf reconnConf = new ReconnConf(5000L, 20);
		TioClientConfig clientConfig = new TioClientConfig(clientHandler, clientListener, reconnConf);
		//设置心跳包时间间隔
		clientConfig.setHeartbeatTimeout(tioProps.getHeartTimeout());
		return clientConfig;
	}

	/**
	 * 服务端一组连接共用的上下文对象
	 * @return
	 */
	@Bean
	public TioServerConfig serverGroupContext() {

		TioServerConfig serverGroupContext = new TioServerConfig(
				tioProps.getServerGroupContextName(),
				serverHandler,
				serverListener);
		serverGroupContext.setHeartbeatTimeout(tioProps.getHeartTimeout());

		return serverGroupContext;
	}

}
