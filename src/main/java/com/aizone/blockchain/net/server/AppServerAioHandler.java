package com.aizone.blockchain.net.server;

import com.aizone.blockchain.core.Block;
import com.aizone.blockchain.core.Transaction;
import com.aizone.blockchain.core.TransactionPool;
import com.aizone.blockchain.db.DBAccess;
import com.aizone.blockchain.encrypt.SignUtils;
import com.aizone.blockchain.encrypt.WalletUtils;
import com.aizone.blockchain.net.base.BaseAioHandler;
import com.aizone.blockchain.net.base.MessagePacket;
import com.aizone.blockchain.net.base.MessagePacketType;
import com.aizone.blockchain.net.base.ServerResponseVo;
import com.aizone.blockchain.utils.SerializeUtils;
import com.aizone.blockchain.wallet.Account;
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

	/**
	 * 处理消息
	 */
	@Override
	public void handler(Packet packet, ChannelContext channelContext) throws Exception {

		MessagePacket messagePacket = (MessagePacket) packet;
		byte type = messagePacket.getType();
		byte[] body = messagePacket.getBody();

		if (body != null) {

			if (body != null) {
				MessagePacket resPacket = new MessagePacket();
				ServerResponseVo responseVo = new ServerResponseVo();
				switch (type) {

					case MessagePacketType.STRING_MESSAGE:
						String str = (String) SerializeUtils.unSerialize(body);
						logger.info("收到客户端请求消息："+str);
						resPacket.setType(MessagePacketType.STRING_MESSAGE);
						resPacket.setBody(SerializeUtils.serialize("收到了你的消息，你的消息是:" + str));
						break;

					case MessagePacketType.REQ_CONFIRM_TRANSACTION:
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
						}
						resPacket.setType(MessagePacketType.RES_CONFIRM_TRANSACTION);
						resPacket.setBody(SerializeUtils.serialize(responseVo));
						break;

					case MessagePacketType.REQ_SYNC_NEXT_BLOCK:

						Integer blockIndex = (Integer) SerializeUtils.unSerialize(body);
						logger.info("收到区块同步请求, 同步区块高度为， {}", blockIndex);
						Optional<Block> block = dbAccess.getBlock(blockIndex);
						if (block.isPresent()) {
							responseVo.setItem(block.get());
							responseVo.setSuccess(true);
						} else {
							responseVo.setSuccess(false);
							responseVo.setItem(null);
							responseVo.setMessage("要同步的区块不存在.");
							logger.info("区块高度 {} 不存在", blockIndex);
						}
						resPacket.setType(MessagePacketType.RES_SYNC_NEXT_BLOCK);
						resPacket.setBody(SerializeUtils.serialize(responseVo));
						break;

					//新区快确认
					case MessagePacketType.REQ_NEW_BLOCK:
						Block newBlock = (Block) SerializeUtils.unSerialize(body);

						logger.info("收到新区块确认请求： {}", newBlock);
						if (checkBlock(newBlock, dbAccess)) {
							logger.info("区块确认成功：{}", newBlock);
							dbAccess.putBlock(newBlock);
							responseVo.setSuccess(true);
						} else {
							responseVo.setSuccess(false);
							responseVo.setMessage("区块校验失败，不合法的区块.");
						}
						responseVo.setItem(newBlock);
						resPacket.setType(MessagePacketType.RES_NEW_BLOCK);
						resPacket.setBody(SerializeUtils.serialize(responseVo));
						break;

					//账户同步
					case MessagePacketType.REQ_NEW_ACCOUNT:
						Account account = (Account) SerializeUtils.unSerialize(body);
						logger.info("收到账号同步请求， {}", account);
						if (WalletUtils.verifyAddress(account.getAddress())) {
							dbAccess.putAccount(account);
							responseVo.setSuccess(true);
						} else {
							responseVo.setSuccess(false);
							responseVo.setMessage("不合法的钱包地址");
						}
						responseVo.setItem(account);
						resPacket.setType(MessagePacketType.RES_NEW_ACCOUNT);
						resPacket.setBody(SerializeUtils.serialize(responseVo));
						break;

						//获取账户列表
					case MessagePacketType.REQ_ACCOUNTS_LIST:
						List<Account> accounts = dbAccess.listAccounts();
						responseVo.setItem(accounts);
						resPacket.setType(MessagePacketType.RES_ACCOUNTS_LIST);
						resPacket.setBody(SerializeUtils.serialize(responseVo));
						break;

					default:
						break;
				} //end of switch

				//发送消息
				Aio.send(channelContext, resPacket);
			}
		}
		return;
	}
}
