package com.aizone.blockchain.net.conf;

import com.aizone.blockchain.net.client.BlockClientAioHandler;
import com.aizone.blockchain.net.client.BlockClientAioListener;
import com.aizone.blockchain.net.server.BlockServerAioHandler;
import com.aizone.blockchain.net.server.BlockServerAioListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.client.intf.ClientAioHandler;
import org.tio.client.intf.ClientAioListener;
import org.tio.server.ServerGroupContext;
import org.tio.server.intf.ServerAioHandler;
import org.tio.server.intf.ServerAioListener;

/**
 * Group context 配置
 * @author yangjian
 * @since 18-4-18
 */
@Configuration
public class GroupContextConfig {

	@Autowired
	TioProperties tioProperties;

	/**
	 * 客户端一组连接共用的上下文对象
	 * @return
	 */
	@Bean
	public ClientGroupContext clientGroupContext() {
		//handler, 包括编码、解码、消息处理
		ClientAioHandler aioHandler = new BlockClientAioHandler();
		//事件监听器
		ClientAioListener aioListener = new BlockClientAioListener();
		//断链后自动连接
		ReconnConf reconnConf = new ReconnConf(5000L, 20);
		ClientGroupContext clientGroupContext = new ClientGroupContext(aioHandler, aioListener, reconnConf);
		//设置心跳包时间间隔
		clientGroupContext.setHeartbeatTimeout(tioProperties.getHeartTimeout());
		return clientGroupContext;
	}

	/**
	 * 服务端一组连接共用的上下文对象
	 * @return
	 */
	@Bean
	public ServerGroupContext serverGroupContext() {

		//handler, 包括编码、解码、消息处理
		ServerAioHandler aioHandler = new BlockServerAioHandler();
		//事件监听器
		ServerAioListener aioListener = new BlockServerAioListener();

		ServerGroupContext serverGroupContext = new ServerGroupContext(
				tioProperties.getServerGroupContextName(),
				aioHandler,
				aioListener);
		serverGroupContext.setHeartbeatTimeout(tioProperties.getHeartTimeout());

		return serverGroupContext;
	}

}
