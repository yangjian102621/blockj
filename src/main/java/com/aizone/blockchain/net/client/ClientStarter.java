package com.aizone.blockchain.net.client;

import com.aizone.blockchain.db.DBUtils;
import com.aizone.blockchain.event.ClientRequestEvent;
import com.aizone.blockchain.net.ApplicationContextProvider;
import com.aizone.blockchain.net.base.BlockPacket;
import com.aizone.blockchain.net.base.Node;
import com.aizone.blockchain.net.conf.TioProperties;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tio.client.AioClient;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.core.Aio;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * 客户端启动程序
 * @author yangjian
 *
 */
@Component
public class ClientStarter {

	@Resource
	private ClientGroupContext clientGroupContext;
	@Autowired
	private TioProperties tioProperties;

	private AioClient aioClient;

	/**
	 * 启动程序入口
	 */
	@PostConstruct
	public void clientStart() throws Exception {

		//加载数据库中的节点数据
		Optional<List<Node>> nodeList = DBUtils.getNodeList();
		aioClient = new AioClient(clientGroupContext);
		if (nodeList.isPresent()) {
			for (Node node : nodeList.get()) {
				addNode(node.getIp(), node.getPort());
			}
		}
	}

	/**
	 * 向服务端打招呼
	 * @param clientChannelContext
	 * @throws Exception
	 */
	private void sayHello(ClientChannelContext clientChannelContext) {
		BlockPacket packet = new BlockPacket();
		try {
			packet.setBody("Fuck you, block chain.".getBytes(BlockPacket.CHARSET));
			Aio.send(clientChannelContext, packet);
		} catch (Exception e) {

		}
	}

	/**
	 * 对客户端群发消息
	 * @param blockPacket
	 */
	public void sendGroup(BlockPacket blockPacket) {

		//对外发出client请求事件
		ApplicationContextProvider.publishEvent(new ClientRequestEvent(blockPacket));
		//发送消息到一个group
		Aio.sendToGroup(clientGroupContext, tioProperties.getClientGroupName(), blockPacket);
	}

	/**
	 * 添加节点
	 * @param serverIp
	 * @param port
	 */
	public void addNode(String serverIp, int port) throws Exception {

		Node node = new Node(serverIp, port);
		ClientChannelContext channelContext = aioClient.connect(node);
		sayHello(channelContext);
		Aio.bindGroup(channelContext, tioProperties.getClientGroupName());

	}
}
