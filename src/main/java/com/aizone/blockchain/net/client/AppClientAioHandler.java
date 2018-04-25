package com.aizone.blockchain.net.client;

import com.aizone.blockchain.core.Block;
import com.aizone.blockchain.core.Transaction;
import com.aizone.blockchain.db.DBAccess;
import com.aizone.blockchain.event.FetchNextBlockEvent;
import com.aizone.blockchain.net.ApplicationContextProvider;
import com.aizone.blockchain.net.base.*;
import com.aizone.blockchain.utils.SerializeUtils;
import com.aizone.blockchain.wallet.Account;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;

import java.util.List;

/**
 * 客户端 AioHandler 实现
 * @author yangjian
 */
@Component
public class AppClientAioHandler extends BaseAioHandler implements ClientAioHandler {

	private static Logger logger = LoggerFactory.getLogger(AppClientAioHandler.class);
	@Autowired
	private DBAccess dbAccess;
	@Autowired
	private AppClient appClient;
	/**
	 * 心跳包
	 */
	private static MessagePacket heartbeatPacket = new MessagePacket(MessagePacketType.STRING_MESSAGE);

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

				//同步最新账户
				case MessagePacketType.RES_NEW_ACCOUNT:
					this.newAccount(body);
					break;

				//同步所有的账号
				case MessagePacketType.RES_ACCOUNTS_LIST:
					this.getAccountList(body);
					break;

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

		logger.info("收到交易确认响应");
		ServerResponseVo responseVo = (ServerResponseVo) SerializeUtils.unSerialize(body);
		Transaction tx = (Transaction) responseVo.getItem();
		if (responseVo.isSuccess()) {
			logger.info("交易确认成功， {}", tx);
		} else {
			logger.error("交易确认失败, {}", tx);
		}
	}

	/**
	 * 同步下一个区块
	 * @param body
	 */
	public void fetchNextBlock(byte[] body) {

		ServerResponseVo responseVo = (ServerResponseVo) SerializeUtils.unSerialize(body);
		if (!responseVo.isSuccess()) {
			logger.error("区块同步失败, "+responseVo.getMessage());
			return;
		}
		Block block = (Block) responseVo.getItem();
		//当前高度的区块已经存在，略过
		if (dbAccess.getBlock(block.getHeader().getIndex()).isPresent()) {
			return;
		}
		if (checkBlock(block, dbAccess)) {
			//更新最新区块高度
			Optional<Object> lastBlockIndex = dbAccess.getLastBlockIndex();
			if (lastBlockIndex.isPresent()) {
				Integer blockIndex = (Integer) lastBlockIndex.get();
				if (blockIndex  < block.getHeader().getIndex()) {
					dbAccess.putBlock(block);
					dbAccess.putLastBlockIndex(block.getHeader().getIndex());
				}
			} else {
				dbAccess.putBlock(block);
				dbAccess.putLastBlockIndex(block.getHeader().getIndex());
			}
			logger.info("区块同步成功， {}", block.getHeader());
			//继续同步下一个区块
			ApplicationContextProvider.publishEvent(new FetchNextBlockEvent(0));
		} else {
			logger.error("区块同步失败，{}", block.getHeader());
			//重新发起同步请求
			//ApplicationContextProvider.publishEvent(new FetchNextBlockEvent(block.getHeader().getIndex()-1));
		}
	}

	/**
	 * 新区块确认
	 * @param body
	 */
	public void newBlock(byte[] body) {
		ServerResponseVo responseVo = (ServerResponseVo) SerializeUtils.unSerialize(body);
		Block newBlock = (Block) responseVo.getItem();
		if (responseVo.isSuccess()) {
			logger.info("区块确认成功, {}", newBlock);
		} else {
			logger.error("区块确认失败, {}, {}", responseVo.getMessage(), newBlock);
		}
	}

	/**
	 * 同步新账户
	 * @param body
	 */
	public void newAccount(byte[] body) {
		ServerResponseVo responseVo = (ServerResponseVo) SerializeUtils.unSerialize(body);
		Account account = (Account) responseVo.getItem();
		if (responseVo.isSuccess()) {
			logger.info("新账户同步账户成功， {}", account);
		} else {
			logger.error("新账户同步账户失败, {}", account);
		}
	}

	/**
	 * 更新账户列表
	 * @param body
	 */
	public void getAccountList(byte[] body) {

		ServerResponseVo responseVo = (ServerResponseVo) SerializeUtils.unSerialize(body);
		if (!responseVo.isSuccess()) {
			return;
		}
		List<Account> accounts = (List<Account>) responseVo.getItem();
		for (Account e : accounts) {
			Optional<Account> acc = dbAccess.getAccount(e.getAddress());
			//已有账号跳过
			if (acc.isPresent()) {
				logger.info("账户已存在：{}", e);
				continue;
			}
			if (dbAccess.putAccount(e)) {
				logger.info("同步账户成功：{}", e);
			}

		}
	}

	/**
	 * 获取节点列表
	 * @param body
	 */
	public void getNodeList(byte[] body) throws Exception {

		ServerResponseVo responseVo = (ServerResponseVo) SerializeUtils.unSerialize(body);
		if (!responseVo.isSuccess()) {
			return;
		}
		List<Node> nodes = (List<Node>) responseVo.getItem();
		for (Node node : nodes) {
			dbAccess.addNode(node);
			appClient.addNode(node.getIp(), node.getPort());
		}

	}

	/**
	 * 此方法如果返回null，框架层面则不会发心跳；如果返回非null，框架层面会定时发本方法返回的消息包
	 * @return
	 */
	@Override
	public MessagePacket heartbeatPacket() {
		return heartbeatPacket;
	}
}
