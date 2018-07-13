package com.ppblock.blockchain.net.client;

import com.google.common.base.Optional;
import com.ppblock.blockchain.db.DBAccess;
import com.ppblock.blockchain.event.FetchNextBlockEvent;
import com.ppblock.blockchain.net.ApplicationContextProvider;
import com.ppblock.blockchain.net.base.MessagePacket;
import com.ppblock.blockchain.net.base.MessagePacketType;
import com.ppblock.blockchain.net.base.Node;
import com.ppblock.blockchain.net.conf.TioProperties;
import com.ppblock.blockchain.utils.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
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
		List<Node> nodes = null;
		if (nodeList.isPresent()) {
			nodes = nodeList.get();

			//初始化配置 properties 中的节点
		} else if (null != tioProperties.getNodes()) {
			nodes = tioProperties.getNodes();
		}
		// 添加节点
		for (Node node : nodes) {
			addNode(node.getIp(), node.getPort());
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
		dbAccess.addNode(node);
		logger.info("添加节点成功, {}", node);
	}

	/**
	 * 启动的时候自动开始区块同步
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void fetchNextBlock() {
		ApplicationContextProvider.publishEvent(new FetchNextBlockEvent(0));

	}

	/**
	 * 同步账户列表
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void fetchAccounts() {
		MessagePacket packet = new MessagePacket();
		packet.setType(MessagePacketType.REQ_ACCOUNTS_LIST);
		packet.setBody(SerializeUtils.serialize(MessagePacket.FETCH_ACCOUNT_LIST_SYMBOL));
		sendGroup(packet);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void fetchNodeList() {

		logger.info("++++++++++++++++++++++++++ 开始获取在线节点 +++++++++++++++++++++++++++");
		MessagePacket packet = new MessagePacket();
		packet.setType(MessagePacketType.REQ_NODE_LIST);
		packet.setBody(SerializeUtils.serialize(MessagePacket.FETCH_NODE_LIST_SYMBOL));
		sendGroup(packet);
	}
}
