package org.rockyang.jblock.chain.sync;

import org.rockyang.jblock.chain.service.ChainService;
import org.rockyang.jblock.utils.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * message request handler
 * this handler will process the message response from the server
 * @author yangjian
 */
@Component
public class ClientHandler {
	private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

	private final ChainService chainService;

	public ClientHandler(ChainService chainService)
	{
		this.chainService = chainService;
	}

	public void SyncBlock(byte[] body)
	{

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

	// new block confirm
	public void newBlock(byte[] body)
	{
		RespVo respVo = (RespVo) SerializeUtils.unSerialize(body);
		Object blockIndex = respVo.getItem();

		if (!respVo.isSuccess() && chainService.isBlockValidated(blockIndex)) {
			logger.error("block confirm failed, remove it, {}", blockIndex);
			chainService.unValidateBlock(blockIndex);
		}
	}

	// new message validation
	public void newMessage(byte[] body)
	{

		RespVo respVo = (RespVo) SerializeUtils.unSerialize(body);
		String msgCid = (String) respVo.getItem();

		if (!respVo.isSuccess()) {
			logger.error("message confirm failed, ");
			// remove message from message pool
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
