package org.rockyang.jblock.chain.sync;

import org.rockyang.jblock.chain.Block;
import org.rockyang.jblock.chain.MessagePool;
import org.rockyang.jblock.chain.event.SyncBlockEvent;
import org.rockyang.jblock.chain.service.BlockService;
import org.rockyang.jblock.net.ApplicationContextProvider;
import org.rockyang.jblock.utils.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * message request handler
 * this handler will process the message response from the server
 *
 * @author yangjian
 */
@Component
public class ClientHandler {
	private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

	private final BlockService blockService;
	private final MessagePool messagePool;

	public ClientHandler(BlockService blockService,
	                     MessagePool messagePool)
	{
		this.blockService = blockService;
		this.messagePool = messagePool;
	}

	public void syncBlock(byte[] body)
	{

		RespVo respVo = (RespVo) SerializeUtils.unSerialize(body);
		if (!respVo.isSuccess()) {
			logger.warn("failed to sync block, {}", respVo.getItem());
			return;
		}
		Block newBlock = (Block) respVo.getItem();
		Block block = blockService.getBlock(newBlock.getHeader().getHash());
		// keep the older block
		if (block != null && block.getHeader().getTimestamp() <= newBlock.getHeader().getTimestamp()) {
			logger.info("block {} is already validate, skip it.", newBlock.getHeader().getHeight());
			// sync the next block
			ApplicationContextProvider.publishEvent(new SyncBlockEvent(newBlock.getHeader().getHeight() + 1));
			return;
		}

		if (blockService.checkBlock(newBlock, respVo)) {
			blockService.markBlockAsValidated(newBlock);
			if (block != null) {
				blockService.unmarkBlockAsValidated(block.getHeader().getHash());
			}
			logger.info("sync block {} successfully, hash: {}", newBlock.getHeader().getHeight(), newBlock.getHeader().getHash());
			ApplicationContextProvider.publishEvent(new SyncBlockEvent(newBlock.getHeader().getHeight() + 1));
		} else {
			logger.warn("Invalid block, height: {}, message: {}", newBlock.getHeader().getHeight(), respVo.getMessage());
		}
	}

	// new block confirm
	public void newBlock(byte[] body)
	{
		RespVo respVo = (RespVo) SerializeUtils.unSerialize(body);
		String blockHash = (String) respVo.getItem();

//		if (!respVo.isSuccess() && blockService.isBlockValidated(blockHash)) {
//			logger.error("block confirm failed, drop it, {}", blockHash);
//			blockService.unmarkBlockAsValidated(blockHash);
//		}
	}

	// new message validation
	public void newMessage(byte[] body)
	{
		RespVo respVo = (RespVo) SerializeUtils.unSerialize(body);
		String msgCid = (String) respVo.getItem();
		if (!respVo.isSuccess()) {
			logger.error("message {} confirm failed, drop it", msgCid);
			// remove message from message pool
			messagePool.removeMessage(msgCid);
		}
	}
}
