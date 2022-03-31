package org.rockyang.jblock.chain.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

/**
 * @author yangjian
 */
public class NodeEventListener {
	private static Logger logger = LoggerFactory.getLogger(NodeEventListener.class);
	/**
	 * 同步节点列表
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void fetchNodeList()
	{
//		logger.info("++++++++++++++++++++++++++ 开始获取在线节点 +++++++++++++++++++++++++++");
//		MessagePacket packet = new MessagePacket();
//		packet.setType(MessagePacketType.REQ_NODE_LIST);
//		packet.setBody(SerializeUtils.serialize(MessagePacket.FETCH_NODE_LIST_SYMBOL));
//		sendGroup(packet);
	}
}
