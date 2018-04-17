package com.aizone.blockchain.net.server;

import com.aizone.blockchain.net.base.Const;
import org.springframework.stereotype.Component;
import org.tio.server.AioServer;
import org.tio.server.ServerGroupContext;
import org.tio.server.intf.ServerAioHandler;
import org.tio.server.intf.ServerAioListener;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * 服务端启动程序
 * @author yangjian
 */
@Component
public class BlockServerStarter {

	/**
	 * handler, 包括编码、解码、消息处理
	 */
	public static ServerAioHandler aioHandler = new BlockServerAioHandler();

	/**
	 * 事件监听器，可以为null，但建议自己实现该接口，可以参考showcase了解些接口
	 */
	public static ServerAioListener aioListener = new BlockServerAioListener();

	/**
	 * 一组连接共用的上下文对象
	 */
	public static ServerGroupContext serverGroupContext = new ServerGroupContext("hello-tio-server", aioHandler, aioListener);

	/**
	 * aioServer对象
	 */
	public static AioServer aioServer = new AioServer(serverGroupContext);

	/**
	 * 有时候需要绑定ip，不需要则null
	 */
	public static String serverIp = null;

	/**
	 * 监听的端口
	 */
	public static int serverPort = Const.PORT;

	/**
	 * 启动程序入口
	 */
	@PostConstruct
	public void serverStart() throws IOException {

		serverGroupContext.setHeartbeatTimeout(Const.TIMEOUT);
		aioServer.start(serverIp, serverPort);
	}
}