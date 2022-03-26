package org.rockyang.jblock.net.server;

import org.rockyang.jblock.chain.Block;
import org.rockyang.jblock.chain.Message;
import org.rockyang.jblock.chain.MessageExecutor;
import org.rockyang.jblock.chain.MessagePool;
import org.rockyang.jblock.conf.AppConfig;
import org.rockyang.jblock.db.Datastore;
import org.rockyang.jblock.net.base.BaseHandler;
import org.rockyang.jblock.net.base.MessagePacket;
import org.rockyang.jblock.net.base.MessagePacketType;
import org.rockyang.jblock.net.base.ServerResponseVo;
import org.rockyang.jblock.utils.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.core.TioConfig;
import org.tio.core.intf.Packet;
import org.tio.server.intf.TioServerHandler;

import java.nio.ByteBuffer;

/**
 * 服务端 AioHandler 实现
 * @author yangjian
 */
@Component
public class AppServerHandler extends BaseHandler implements TioServerHandler {

	private static Logger logger = LoggerFactory.getLogger(AppServerHandler.class);
	@Autowired
	private Datastore dataStore;
	@Autowired
	private MessagePool messagePool;
	@Autowired
	private MessageExecutor executor;
	@Autowired
	private AppConfig appConfig;

	@Override
	public ByteBuffer encode(Packet packet, TioConfig tioConfig, ChannelContext channelContext)
	{
		return null;
	}

	/**
	 * 处理消息
	 */
	@Override
	public void handler(Packet packet, ChannelContext channelContext) throws Exception
	{

		MessagePacket messagePacket = (MessagePacket) packet;
		byte type = messagePacket.getType();
		byte[] body = messagePacket.getBody();

		if (body != null) {
			logger.info("请求节点信息， {}", channelContext.getClientNode());
			if (body != null) {
				MessagePacket resPacket = null;
				switch (type) {

					case MessagePacketType.STRING_MESSAGE:
						resPacket = this.stringMessage(body);
						break;

						// 确认交易
					case MessagePacketType.REQ_CONFIRM_TRANSACTION:
						resPacket = this.confirmTransaction(body);
						break;

						// 同步下一个区块
					case MessagePacketType.REQ_SYNC_NEXT_BLOCK:
						resPacket = this.fetchNextBlock(body);
						break;

						// 新区快确认
					case MessagePacketType.REQ_NEW_BLOCK:
						resPacket = this.newBlock(body);
						break;

						//获取节点列表
					case MessagePacketType.REQ_NODE_LIST:
						resPacket = this.getNodeList(body);
						break;

					case MessagePacketType.RES_INC_CONFIRM_NUM:
						resPacket = this.incBlockConfirmNum(body);
						break;

				} //end of switch

				//发送消息
				Tio.send(channelContext, resPacket);
			}
		}
		return;
	}

	/**
	 * 普通字符串
	 * @param body
	 * @return
	 */
	public MessagePacket stringMessage(byte[] body) {

		MessagePacket resPacket = new MessagePacket();
		String str = (String) SerializeUtils.unSerialize(body);
		logger.info("收到客户端请求消息："+str);
		resPacket.setType(MessagePacketType.STRING_MESSAGE);
		resPacket.setBody(SerializeUtils.serialize("收到了你的消息，你的消息是:" + str));

		return resPacket;
	}

	/**
	 * 去人确认交易
	 * @param body
	 */
	public MessagePacket confirmTransaction(byte[] body) throws Exception {

		ServerResponseVo responseVo = new ServerResponseVo();
		MessagePacket resPacket = new MessagePacket();
		Message tx = (Message) SerializeUtils.unSerialize(body);
		logger.info("收到交易确认请求， {}", tx);
		responseVo.setItem(tx);
//		//验证交易
//		if (Sign.verify(Keys.publicKeyDecode(tx.getPublicKey()), tx.getSign(), tx.toSignString())) {
//			responseVo.setSuccess(true);
//			//将交易放入交易池
//			messagePool.addTransaction(tx);
//		} else {
//			responseVo.setSuccess(false);
//			responseVo.setMessage("交易签名错误");
//			logger.info("交易确认失败, 交易签名错误, {}", tx);
//		}
		resPacket.setType(MessagePacketType.RES_CONFIRM_TRANSACTION);
		resPacket.setBody(SerializeUtils.serialize(responseVo));

		return resPacket;
	}

	/**
	 * 获取下一个区块
	 * @param body
	 * @return
	 */
	public MessagePacket fetchNextBlock(byte[] body) {

		ServerResponseVo responseVo = new ServerResponseVo();
		MessagePacket resPacket = new MessagePacket();
		Integer blockIndex = (Integer) SerializeUtils.unSerialize(body);
		logger.info("收到区块同步请求, 同步区块高度为， {}", blockIndex);
//		Optional<Block> block = dataStore.getBlock(blockIndex);
//		if (block.isPresent()) {
//			responseVo.setItem(block.get());
//			responseVo.setSuccess(true);
//		} else {
//			responseVo.setSuccess(false);
//			responseVo.setItem(null);
//			responseVo.setMessage("要同步的区块不存在.{"+blockIndex+"}");
//		}
		resPacket.setType(MessagePacketType.RES_SYNC_NEXT_BLOCK);
		resPacket.setBody(SerializeUtils.serialize(responseVo));

		return resPacket;
	}

	public MessagePacket newBlock(byte[] body) throws Exception {

		ServerResponseVo responseVo = new ServerResponseVo();
		MessagePacket resPacket = new MessagePacket();
		Block newBlock = (Block) SerializeUtils.unSerialize(body);
//		logger.info("收到新区块确认请求： {}", newBlock);
//		if (checkBlock(newBlock, dataStore)) {
//			dataStore.putLastBlockIndex(newBlock.getHeader().getIndex());
//			dataStore.putBlock(newBlock);
//			responseVo.setSuccess(true);
//			//执行区块中的交易，同步账户的余额
//			executor.run(newBlock);
//		} else {
//			logger.error("区块确认失败：{}", newBlock);
//			responseVo.setSuccess(false);
//			responseVo.setMessage("区块校验失败，不合法的区块.");
//		}
//		responseVo.setItem(newBlock);
//		resPacket.setType(MessagePacketType.RES_NEW_BLOCK);
//		resPacket.setBody(SerializeUtils.serialize(responseVo));

		return resPacket;
	}

	/**
	 * 获取节点列表
	 * @param body
	 * @return
	 */
	public MessagePacket getNodeList(byte[] body)
	{
		String message = (String) SerializeUtils.unSerialize(body);
		ServerResponseVo responseVo = new ServerResponseVo();
		MessagePacket resPacket = new MessagePacket();
		logger.info("收到获取节点列表请求");
//		if (Objects.equal(message, MessagePacket.FETCH_NODE_LIST_SYMBOL)) {
//			Optional<List<Node>> nodes = dataStore.getNodeList();
//			if (nodes.isPresent()) {
//				responseVo.setSuccess(true);
//				responseVo.setItem(nodes.get());
//			}
//		} else {
//			responseVo.setSuccess(false);
//		}
		resPacket.setType(MessagePacketType.RES_NODE_LIST);
		resPacket.setBody(SerializeUtils.serialize(responseVo));

		return  resPacket;
	}

	public MessagePacket incBlockConfirmNum(byte[] body)
	{
		ServerResponseVo responseVo = new ServerResponseVo();
		MessagePacket resPacket = new MessagePacket();
		Integer blockIndex = (Integer) SerializeUtils.unSerialize(body);
		logger.info("收到增加区块确认数请求, 同步区块高度为， {}", blockIndex);
//		Optional<Block> blockOptional = dataStore.getBlock(blockIndex);
//		if (blockOptional.isPresent()) {
//			Block block = blockOptional.get();
//			// 增加区块确认数
//			block.setConfirmNum(block.getConfirmNum()+1);
//
//			if (block.getConfirmNum() >= appConfig.getMinConfirmNum()) {
//				// 更改当前区块所有的交易状态
//				for (Message message : block.getBody().getTransactions()) {
//					message.setStatus(MessageStatus.SUCCESS);
//				}
//			}
//			dataStore.putBlock(block); // 更新区块
//			responseVo.setSuccess(true);
//		} else {
//			responseVo.setSuccess(false);
//			responseVo.setMessage("区块高度不存在.{"+blockIndex+"}");
//		}
//		resPacket.setType(MessagePacketType.RES_INC_CONFIRM_NUM);
//		resPacket.setBody(SerializeUtils.serialize(responseVo));

		return resPacket;
	}
}
