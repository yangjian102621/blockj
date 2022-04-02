package org.rockyang.jblock.chain.sync;

import org.apache.commons.lang3.StringUtils;
import org.rockyang.jblock.chain.Block;
import org.rockyang.jblock.chain.Message;
import org.rockyang.jblock.chain.service.ChainService;
import org.rockyang.jblock.miner.pow.ProofOfWork;
import org.rockyang.jblock.net.base.MessagePacket;
import org.rockyang.jblock.net.base.MessagePacketType;
import org.rockyang.jblock.utils.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * message response handler
 * reply the message that send from the other client
 * @author yangjian
 */
@Component
public class ServerHandler {

	private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

	private final ChainService chainService;

	public ServerHandler(ChainService chainService)
	{
		this.chainService = chainService;
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

		RespVo responseVo = new RespVo();
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

		RespVo responseVo = new RespVo();
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
//		resPacket.setType(MessagePacketType.RES_SYNC_NEXT_BLOCK);
//		resPacket.setBody(SerializeUtils.serialize(responseVo));

		return resPacket;
	}

	public MessagePacket newBlock(byte[] body) throws Exception {

		RespVo responseVo = new RespVo();
		MessagePacket resPacket = new MessagePacket();
		Block newBlock = (Block) SerializeUtils.unSerialize(body);
		logger.info("收到新区块确认请求： {}", newBlock);
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
		responseVo.setItem(newBlock);
		resPacket.setType(MessagePacketType.RES_NEW_BLOCK);
		resPacket.setBody(SerializeUtils.serialize(responseVo));

		return resPacket;
	}

	/**
	 * 检验区块是否合法
	 * 1. 验证改区块前一个区块是否存在，且 previousHash 是否合法（暂时不做验证）
	 * 2. 验证该区块本身 hash 是否合法
	 */
	public boolean checkBlock(Block block) {

		// @TODO: check the genesis block

		// check the proof of work nonce
		ProofOfWork proofOfWork = ProofOfWork.newProofOfWork(block.getHeader());
		if (!proofOfWork.validate()) {
			return false;
		}

		if (block.getHeader().getHeight() > 1) {
			Block prevBlock = chainService.getBlock(block.getHeader().getHeight()-1);
			if (prevBlock == null || StringUtils.equals(prevBlock.getHeader().getHash(), block.getHeader().getPreviousHash())) {
				return false;
			}
		}

		// @TODO: check the block signature

		return true;

	}

	/**
	 * 获取节点列表
	 * @param body
	 * @return
	 */
	public MessagePacket getNodeList(byte[] body)
	{
		String message = (String) SerializeUtils.unSerialize(body);
		RespVo responseVo = new RespVo();
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
		RespVo responseVo = new RespVo();
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
