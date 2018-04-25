package com.aizone.blockchain.net.server;

import com.aizone.blockchain.core.Block;
import com.aizone.blockchain.core.Transaction;
import com.aizone.blockchain.core.TransactionExecutor;
import com.aizone.blockchain.core.TransactionPool;
import com.aizone.blockchain.db.DBAccess;
import com.aizone.blockchain.encrypt.SignUtils;
import com.aizone.blockchain.encrypt.WalletUtils;
import com.aizone.blockchain.net.base.*;
import com.aizone.blockchain.utils.SerializeUtils;
import com.aizone.blockchain.wallet.Account;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioHandler;

import java.util.List;

/**
 * 服务端 AioHandler 实现
 * @author yangjian
 */
@Component
public class AppServerAioHandler extends BaseAioHandler implements ServerAioHandler {

	private static Logger logger = LoggerFactory.getLogger(AppServerAioHandler.class);
	@Autowired
	private DBAccess dbAccess;
	@Autowired
	private TransactionPool transactionPool;
	@Autowired
	private TransactionExecutor executor;
	/**
	 * 处理消息
	 */
	@Override
	public void handler(Packet packet, ChannelContext channelContext) throws Exception {

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

					case MessagePacketType.REQ_CONFIRM_TRANSACTION:
						resPacket = this.confirmTransaction(body);
						break;

					case MessagePacketType.REQ_SYNC_NEXT_BLOCK:
						resPacket = this.fetchNextBlock(body);
						break;

					//新区快确认
					case MessagePacketType.REQ_NEW_BLOCK:
						resPacket = this.newBlock(body);
						break;

					//账户同步
					case MessagePacketType.REQ_NEW_ACCOUNT:
						resPacket = this.newAccount(body);
						break;

						//获取账户列表
					case MessagePacketType.REQ_ACCOUNTS_LIST:
						resPacket = this.getAccountList(body);
						break;

						//获取节点列表
					case MessagePacketType.REQ_NODE_LIST:
						resPacket = this.getNodeList(body);
						break;

				} //end of switch

				//发送消息
				Aio.send(channelContext, resPacket);
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
		Transaction tx = (Transaction) SerializeUtils.unSerialize(body);
		logger.info("收到交易确认请求， {}", tx);
		responseVo.setItem(tx);
		//验证交易
		if (SignUtils.verify(tx.getPublicKey(), tx.getSign(), tx.toString())) {
			responseVo.setSuccess(true);
			//将交易放入交易池
			transactionPool.addTransaction(tx);
		} else {
			responseVo.setSuccess(false);
			responseVo.setMessage("交易签名错误");
			logger.info("交易确认失败, 交易签名错误, {}", tx);
		}
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
		Optional<Block> block = dbAccess.getBlock(blockIndex);
		if (block.isPresent()) {
			responseVo.setItem(block.get());
			responseVo.setSuccess(true);
		} else {
			responseVo.setSuccess(false);
			responseVo.setItem(null);
			responseVo.setMessage("要同步的区块不存在.{"+blockIndex+"}");
		}
		resPacket.setType(MessagePacketType.RES_SYNC_NEXT_BLOCK);
		resPacket.setBody(SerializeUtils.serialize(responseVo));

		return resPacket;
	}

	public MessagePacket newBlock(byte[] body) throws Exception {

		ServerResponseVo responseVo = new ServerResponseVo();
		MessagePacket resPacket = new MessagePacket();
		Block newBlock = (Block) SerializeUtils.unSerialize(body);
		logger.info("收到新区块确认请求： {}", newBlock);
		if (checkBlock(newBlock, dbAccess)) {
			dbAccess.putLastBlockIndex(newBlock.getHeader().getIndex());
			dbAccess.putBlock(newBlock);
			responseVo.setSuccess(true);
			//执行区块中的交易，同步账户的余额
			executor.run(newBlock);
		} else {
			logger.error("区块确认失败：{}", newBlock);
			responseVo.setSuccess(false);
			responseVo.setMessage("区块校验失败，不合法的区块.");
		}
		responseVo.setItem(newBlock);
		resPacket.setType(MessagePacketType.RES_NEW_BLOCK);
		resPacket.setBody(SerializeUtils.serialize(responseVo));

		return resPacket;
	}

	/**
	 * 同步新账户
	 * @param body
	 * @return
	 */
	public MessagePacket newAccount(byte[] body) {

		ServerResponseVo responseVo = new ServerResponseVo();
		MessagePacket resPacket = new MessagePacket();
		Account account = (Account) SerializeUtils.unSerialize(body);
		logger.info("收到新账户同步请求： {}", account);
		if (WalletUtils.verifyAddress(account.getAddress())) {
			dbAccess.putAccount(account);
			responseVo.setSuccess(true);
		} else {
			responseVo.setSuccess(false);
			responseVo.setMessage("不合法的钱包地址");
			logger.error("新账户同步确认失败, 不合法的钱包地址, {}", account);
		}
		responseVo.setItem(account);
		resPacket.setType(MessagePacketType.RES_NEW_ACCOUNT);
		resPacket.setBody(SerializeUtils.serialize(responseVo));

		return resPacket;
	}

	/**
	 * 获取账户列表
	 * @return
	 */
	public MessagePacket getAccountList(byte[] body) {

		String message = (String) SerializeUtils.unSerialize(body);
		ServerResponseVo responseVo = new ServerResponseVo();
		MessagePacket resPacket = new MessagePacket();
		logger.info("收到获取账户列表请求");
		if (Objects.equal(message, MessagePacket.FETCH_ACCOUNT_LIST_SYMBOL)) {
			List<Account> accounts = dbAccess.listAccounts();
			responseVo.setSuccess(true);
			responseVo.setItem(accounts);
		} else {
			responseVo.setSuccess(false);
		}
		resPacket.setType(MessagePacketType.RES_ACCOUNTS_LIST);
		resPacket.setBody(SerializeUtils.serialize(responseVo));

		return  resPacket;
	}

	/**
	 * 获取节点列表
	 * @param body
	 * @return
	 */
	public MessagePacket getNodeList(byte[] body) {
		String message = (String) SerializeUtils.unSerialize(body);
		ServerResponseVo responseVo = new ServerResponseVo();
		MessagePacket resPacket = new MessagePacket();
		logger.info("收到获取节点列表请求");
		if (Objects.equal(message, MessagePacket.FETCH_NODE_LIST_SYMBOL)) {
			Optional<List<Node>> nodes = dbAccess.getNodeList();
			if (nodes.isPresent()) {
				responseVo.setSuccess(true);
				responseVo.setItem(nodes.get());
			}
		} else {
			responseVo.setSuccess(false);
		}
		resPacket.setType(MessagePacketType.RES_NODE_LIST);
		resPacket.setBody(SerializeUtils.serialize(responseVo));

		return  resPacket;
	}
}
