package org.rockyang.jblock.net.server;

import org.rockyang.jblock.net.conf.TioProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tio.server.TioServer;
import org.tio.server.TioServerConfig;

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
	private TioServerConfig serverConfig;
	@Autowired
	private TioProps properties;

	/**
	 * 网络服务端程序入口
	 */
	@PostConstruct
	public void serverStart() throws IOException {

		TioServer server = new TioServer(serverConfig);
		//本机启动服务
		server.start(properties.getServerIp(), properties.getServerPort());
	}
}