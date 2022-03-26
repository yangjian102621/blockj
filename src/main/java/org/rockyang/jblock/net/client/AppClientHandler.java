package org.rockyang.jblock.net.client;

import org.rockyang.jblock.chain.Block;
import org.rockyang.jblock.chain.MessageExecutor;
import org.rockyang.jblock.chain.MessagePool;
import org.rockyang.jblock.db.Datastore;
import org.rockyang.jblock.event.BlockConfirmNumEvent;
import org.rockyang.jblock.net.ApplicationContextProvider;
import org.rockyang.jblock.net.base.BaseHandler;
import org.rockyang.jblock.net.base.MessagePacket;
import org.rockyang.jblock.net.base.MessagePacketType;
import org.rockyang.jblock.net.base.ServerResponseVo;
import org.rockyang.jblock.utils.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tio.client.intf.TioClientHandler;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端 AioHandler 实现
 * @author yangjian
 */
@Component
public class AppClientHandler extends BaseHandler implements TioClientHandler {

	private static Logger logger = LoggerFactory.getLogger(AppClientHandler.class);
	@Autowired
	private Datastore dataStore;
	@Autowired
	private AppClient appClient;
	@Autowired
	private MessageExecutor executor;
	@Autowired
	private MessagePool messagePool;

	// 已确认区块
	private static ConcurrentHashMap<String, Integer> confirmedBlocks = new ConcurrentHashMap<>(16);

	/**
	 * 心跳包
	 */
	private static final MessagePacket heartbeatPacket = new MessagePacket(MessagePacketType.STRING_MESSAGE);
	/**
	 * 处理消息
	 */
	@Override
	public void handler(Packet packet, ChannelContext channelContext) throws Exception {

		MessagePacket messagePacket = (MessagePacket) packet;
		byte[] body = messagePacket.getBody();
		byte type = messagePacket.getType();
		if (body != null) {
			logger.info("响应节点信息， {}", channelContext.getServerNode());
			switch (type) {
				//发送字符串信息
				case MessagePacketType.STRING_MESSAGE:
					String str = (String) SerializeUtils.unSerialize(body);
					logger.info("收到服务端确认消息："+str);
					break;

					//确认交易回复
				case MessagePacketType.RES_CONFIRM_TRANSACTION:
					this.confirmTransaction(body);
					break;

					//同步区块回复
				case MessagePacketType.RES_SYNC_NEXT_BLOCK:
					this.fetchNextBlock(body);
					break;

					//请求生成新的区块
				case MessagePacketType.RES_NEW_BLOCK:
					this.newBlock(body);
					break;

					// 同步节点信息
				case MessagePacketType.RES_NODE_LIST:
					this.getNodeList(body);
					break;

			} //end of switch

		}
		return;
	}

	/**
	 * 交易确认
	 * @param body
	 */
	public void confirmTransaction(byte[] body) {

//		logger.info("收到交易确认响应");
//		ServerResponseVo responseVo = (ServerResponseVo) SerializeUtils.unSerialize(body);
//		Message tx = (Message) responseVo.getItem();
//		if (responseVo.isSuccess()) {
//			logger.info("交易确认成功， {}", tx);
//		} else {
//			// 将非法交易移除交易池
//			messagePool.removeTransaction(tx.getTxHash());
//			logger.error("交易确认失败, {}", tx);
//		}
	}

	/**
	 * 同步下一个区块
	 * @param body
	 */
	public void fetchNextBlock(byte[] body) throws Exception {

//		ServerResponseVo responseVo = (ServerResponseVo) SerializeUtils.unSerialize(body);
//		if (!responseVo.isSuccess()) {
//			logger.warn("区块同步失败, "+responseVo.getMessage());
//			return;
//		}
//		Block block = (Block) responseVo.getItem();
//		//当前高度的区块已经存在，略过
//		if (dataStore.getBlock(block.getHeader().getIndex()).isPresent()) {
//			logger.info("当前高度 {} 的区块已经存在", block.getHeader().getIndex());
//			return;
//		}
//		if (checkBlock(block, dataStore)) {
//			//更新最新区块高度
//			Optional<Object> lastBlockIndex = dataStore.getLastBlockIndex();
//			if (lastBlockIndex.isPresent()) {
//				Integer blockIndex = (Integer) lastBlockIndex.get();
//				if (blockIndex  < block.getHeader().getIndex()) {
//					dataStore.putBlock(block);
//					dataStore.putLastBlockIndex(block.getHeader().getIndex());
//				}
//			} else {
//				// 创世区块
//				dataStore.putBlock(block);
//				dataStore.putLastBlockIndex(block.getHeader().getIndex());
//			}
//			logger.info("区块同步成功， {}", block.getHeader());
//
//			// 执行区块中的交易
//			executor.run(block);
//
//			//继续同步下一个区块
//			ApplicationContextProvider.publishEvent(new FetchNextBlockEvent(block.getHeader().getIndex()+1));
//		} else {
//			logger.error("区块校验失败，重新发起同步 {}", block.getHeader());
//			//重新发起同步请求
//			ApplicationContextProvider.publishEvent(new FetchNextBlockEvent(block.getHeader().getIndex()));
//		}
	}

	/**
	 * 新区块确认
	 * @param body
	 */
	public void newBlock(byte[] body) throws Exception
	{
		ServerResponseVo responseVo = (ServerResponseVo) SerializeUtils.unSerialize(body);
		Block newBlock = (Block) responseVo.getItem();

		if (responseVo.isSuccess()) {
			synchronized (this) {
				Integer confirmedCounter = confirmedBlocks.get(newBlock.getCid());
				if (null == confirmedCounter) {
					// 执行区块中的交易
					executor.run(newBlock);
					confirmedCounter = 0;
				}
				// 更新当前区块确认数
				newBlock.setConfirmNum(confirmedCounter+1);
				confirmedBlocks.put(newBlock.getHeader().getHash(), confirmedCounter+1);
				// 更新数据库
				dataStore.putBlock(newBlock);
				logger.info("区块确认成功, {}", newBlock);

				// 同步其他节点的区块确认数
				ApplicationContextProvider.publishEvent(new BlockConfirmNumEvent(newBlock.getHeader().getIndex()));
			}
		} else {
			logger.error("区块确认失败, {}, {}", responseVo.getMessage(), newBlock);
		}
	}

	/**
	 * 获取节点列表
	 * @param body
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void getNodeList(byte[] body) throws Exception {

//		ServerResponseVo responseVo = (ServerResponseVo) SerializeUtils.unSerialize(body);
//		if (!responseVo.isSuccess()) {
//			return;
//		}
//		List<Node> nodes = (List<Node>) responseVo.getItem();
//		for (Node node : nodes) {
//			// fix bug https://gitee.com/blackfox/blockchain-java/issues/IWSPJ
//			if (dataStore.addNode(node)) {
//				appClient.addNode(node.getIp(), node.getPort());
//			}
//		}

	}

}
