package org.rockyang.jblock.net.server;

import org.rockyang.jblock.net.conf.TioProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tio.server.AioServer;
import org.tio.server.ServerGroupContext;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;

/**
 * 服务端启动程序
 * @author yangjian
 */
@Component
public class AppServer {

	@Resource
	private ServerGroupContext serverGroupContext;
	@Autowired
	private TioProps properties;

	/**
	 * 网络服务端程序入口
	 */
	@PostConstruct
	public void serverStart() throws IOException {

		AioServer aioServer = new AioServer(serverGroupContext);
		//本机启动服务
		aioServer.start(properties.getServerIp(), properties.getServerPort());
	}
}