package org.rockyang.jblock.chain.listener;

import org.rockyang.jblock.chain.event.PeerConnectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;

/**
 * @author yangjian
 */
public class PeerEventListener {
	private static Logger logger = LoggerFactory.getLogger(PeerEventListener.class);

	@EventListener(PeerConnectEvent.class)
	public void fetchNodeList()
	{
//		logger.info("++++++++++++++++++++++++++ 开始获取在线节点 +++++++++++++++++++++++++++");
//		MessagePacket packet = new MessagePacket();
//		packet.setType(MessagePacketType.REQ_NODE_LIST);
//		packet.setBody(SerializeUtils.serialize(MessagePacket.FETCH_NODE_LIST_SYMBOL));
//		sendGroup(packet);
	}
}
