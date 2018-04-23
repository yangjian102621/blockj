package com.aizone.blockchain.net.client;

import com.aizone.blockchain.db.DBAccess;
import com.aizone.blockchain.net.base.MessagePacket;
import com.aizone.blockchain.net.base.Node;
import com.aizone.blockchain.net.conf.TioProperties;
import com.aizone.blockchain.utils.SerializeUtils;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class AppClient {

	@Resource
	private ClientGroupContext clientGroupContext;
	@Autowired
	private TioProperties tioProperties;

	private AioClient aioClient;
	@Autowired
	private DBAccess dbAccess;

	private static Logger logger = LoggerFactory.getLogger(AppClient.class);

	/**
	 * 启动程序入口
	 */
	@PostConstruct
	public void clientStart() throws Exception {

		aioClient = new AioClient(clientGroupContext);
		//加载数据库中的节点数据
		Optional<List<Node>> nodeList = dbAccess.getNodeList();

		if (nodeList.isPresent()) {
			for (Node node : nodeList.get()) {
				addNode(node.getIp(), node.getPort());
			}
		}
	}

	/**
	 * 发送消息到一个group
	 * @param messagePacket
	 */
	public void sendGroup(MessagePacket messagePacket) {
		Aio.sendToGroup(clientGroupContext, tioProperties.getClientGroupName(), messagePacket);
	}

	/**
	 * 添加节点
	 * @param serverIp
	 * @param port
	 */
	public void addNode(String serverIp, int port) throws Exception {

		Node node = new Node(serverIp, port);
		ClientChannelContext channelContext = aioClient.connect(node);
		Aio.send(channelContext, new MessagePacket(SerializeUtils.serialize(MessagePacket.HELLO_MESSAGE)));
		Aio.bindGroup(channelContext, tioProperties.getClientGroupName());
		logger.info("添加节点成功, {}", node);
	}

//	@EventListener(ApplicationReadyEvent.class)
//	public void fetchNextBlock() {
//		ApplicationContextProvider.publishEvent(new FetchNextBlockEvent(null));
//	}
}
