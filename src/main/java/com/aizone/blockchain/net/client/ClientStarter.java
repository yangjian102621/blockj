package com.aizone.blockchain.net.client;

import com.aizone.blockchain.net.base.BlockPacket;
import com.aizone.blockchain.net.base.Const;
import org.springframework.stereotype.Component;
import org.tio.client.AioClient;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.client.intf.ClientAioHandler;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Aio;
import org.tio.core.Node;

import javax.annotation.PostConstruct;

/**
 * 客户端启动程序
 * @author yangjian
 *
 */
@Component
public class ClientStarter {

	/**
	 * 服务器节点
	 */
	public static Node serverNode = new Node(Const.SERVER, Const.PORT);

	/**
	 * handler, 包括编码、解码、消息处理
	 */
	public static ClientAioHandler aioClientHandler = new BlockClientAioHandler();

	/**
	 * 事件监听器，可以为null，但建议自己实现该接口，可以参考showcase了解些接口
	 */
	public static ClientAioListener aioListener = new BlockClientAioListener();

	/**
	 * 断链后自动连接的，不想自动连接请设为null
	 */
	private static ReconnConf reconnConf = new ReconnConf(5000L);

	/**
	 * 一组连接共用的上下文对象
	 */
	public ClientGroupContext clientGroupContext = new ClientGroupContext(aioClientHandler, aioListener, reconnConf);

	public AioClient aioClient = null;
	public ClientChannelContext clientChannelContext = null;

	/**
	 * 启动程序入口
	 */
	@PostConstruct
	public void clientStart() throws Exception {

		clientGroupContext.setHeartbeatTimeout(Const.TIMEOUT);
		aioClient = new AioClient(clientGroupContext);
		clientChannelContext = aioClient.connect(serverNode);
		//连上后，发条消息玩玩
		send();
	}

	private void send() throws Exception {
		BlockPacket packet = new BlockPacket();
		packet.setBody("hello world".getBytes(BlockPacket.CHARSET));
		Aio.send(clientChannelContext, packet);
	}
}
