package com.aizone.blockchain.net.client;

import com.aizone.blockchain.core.Block;
import com.aizone.blockchain.db.DBUtils;
import com.aizone.blockchain.mine.pow.ProofOfWork;
import com.aizone.blockchain.net.base.BaseAioHandler;
import com.aizone.blockchain.net.base.MessagePacket;
import com.aizone.blockchain.net.base.MessagePacketType;
import com.aizone.blockchain.utils.SerializeUtils;
import com.aizone.blockchain.vo.TransactionConfirmVo;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;

/**
 * 客户端 AioHandler 实现
 * @author yangjian
 */
@Component
public class AppClientAioHandler extends BaseAioHandler implements ClientAioHandler {

	private static Logger logger = LoggerFactory.getLogger(AppClientAioHandler.class);

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
			switch (type) {
				case MessagePacketType.STRING_MESSAGE:
					String str = (String) SerializeUtils.unSerialize(body);
					logger.info("收到消息："+str);
					break;

				case MessagePacketType.RES_CONFRIM_TRANSACTION:
					logger.info("收到交易确认响应");
					TransactionConfirmVo confirmVo = (TransactionConfirmVo) SerializeUtils.unSerialize(body);
					if (confirmVo.isConfirmed()) {
						logger.info("交易确认成功， {}", confirmVo.getTransaction());
					} else {
						logger.error("交易确认失败, {}", confirmVo.getTransaction());
					}
					break;

				case MessagePacketType.RES_SYNC_NEXT_BLOCK:
					Block block = (Block) SerializeUtils.unSerialize(body);
					logger.info("收到区块同步回应, 区块头信息为， {}", block.getHeader());
					/**
					 * 验证区块是否合法
					 * 1. 验证改区块前一个区块是否存在，且 previousHash 是否合法（暂时不做验证）
					 * 2. 验证该区块本身 hash 是否合法
					 */
					boolean blockValidate = true;
					if (block.getHeader().getIndex() > 1) {
						Optional<Block> prevBlock = DBUtils.getBlock(block.getHeader().getIndex()-1);
						boolean check = prevBlock.get().getHeader().getHash().equals(block.getHeader()
								.getPreviousHash());
						if (prevBlock.isPresent()
								&& !check) {
							blockValidate = false;
						}
					}
					//是否符合工作量证明
					ProofOfWork proofOfWork = ProofOfWork.newProofOfWork(block);
					if (!proofOfWork.validate()) {
						blockValidate = false;
					}

					if (blockValidate && DBUtils.putBlock(block)) {
						//更新最新区块高度
						DBUtils.putLastBlockIndex(block.getHeader().getIndex());
						logger.info("区块同步成功， {}", block.getHeader());
					} else {
						logger.error("区块同步失败， {}", block.getHeader());
					}

				default:
					break;
			} //end of switch

		}

		return;
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
