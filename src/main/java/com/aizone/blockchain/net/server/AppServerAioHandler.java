package com.aizone.blockchain.net.server;

import com.aizone.blockchain.core.Block;
import com.aizone.blockchain.core.Transaction;
import com.aizone.blockchain.db.DBUtils;
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
import org.springframework.stereotype.Component;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioHandler;

/**
 * 服务端 AioHandler 实现
 * @author yangjian
 */
@Component
public class AppServerAioHandler extends BaseAioHandler implements ServerAioHandler {

	private static Logger logger = LoggerFactory.getLogger(AppServerAioHandler.class);

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
						} else {
							responseVo.setSuccess(false);
						}
						resPacket.setType(MessagePacketType.RES_CONFIRM_TRANSACTION);
						resPacket.setBody(SerializeUtils.serialize(responseVo));
						break;

					case MessagePacketType.REQ_SYNC_NEXT_BLOCK:

						Integer blockIndex = (Integer) SerializeUtils.unSerialize(body);
						logger.info("收到区块同步请求, 同步区块高度为， {}", blockIndex);
						Optional<Block> block = DBUtils.getBlock(blockIndex);
						if (block.isPresent()) {
							responseVo.setItem(block.get());
							responseVo.setSuccess(true);
						} else {
							responseVo.setSuccess(false);
							responseVo.setItem(null);
						}
						resPacket.setType(MessagePacketType.RES_SYNC_NEXT_BLOCK);
						resPacket.setBody(SerializeUtils.serialize(responseVo));
						break;

					//账户同步
					case MessagePacketType.REQ_NEW_ACCOUNT:
						Account account = (Account) SerializeUtils.unSerialize(body);
						if (WalletUtils.verifyAddress(account.getAddress())) {
							DBUtils.putAccount(account);
							responseVo.setSuccess(true);
						} else {
							responseVo.setSuccess(false);
						}
						responseVo.setItem(account);
						resPacket.setType(MessagePacketType.RES_NEW_ACCOUNT);
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
