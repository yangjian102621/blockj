package org.rockyang.jblock.chain.sync;

import org.rockyang.jblock.base.model.Block;
import org.rockyang.jblock.base.utils.SerializeUtils;
import org.rockyang.jblock.chain.BlockPool;
import org.rockyang.jblock.chain.MessagePool;
import org.rockyang.jblock.chain.event.SyncBlockEvent;
import org.rockyang.jblock.chain.service.BlockService;
import org.rockyang.jblock.conf.ApplicationContextProvider;
import org.rockyang.jblock.vo.PacketVo;
import org.rockyang.jblock.vo.Result;
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
	private final BlockPool blockPool;

	public ClientHandler(BlockService blockService,
	                     MessagePool messagePool,
	                     BlockPool blockPool)
	{
		this.blockService = blockService;
		this.messagePool = messagePool;
		this.blockPool = blockPool;
	}

	public void syncBlock(byte[] body) throws Exception
	{
		PacketVo packetVo = (PacketVo) SerializeUtils.unSerialize(body);
		if (!packetVo.isSuccess()) {
			logger.warn("failed to sync block, {}", packetVo.getMessage());
			// @TODO: retry it later?
			return;
		}
		if (packetVo.getItem() == null) {
			logger.info("chain sync complete");
			return;
		}
		Block block = (Block) packetVo.getItem();
		if (blockService.isBlockValidated(block)) {
			logger.info("block {} is already validate, skip it.", block.getHeader().getHeight());
			// sync the next block
			ApplicationContextProvider.publishEvent(new SyncBlockEvent(block.getHeader().getHeight() + 1));
			return;
		}

		Result result = blockService.checkBlock(block);
		if (result.isOk()) {
			blockService.markBlockAsValidated(block);
			logger.info("sync block successfully, height: {}", block.getHeader().getHeight());
			ApplicationContextProvider.publishEvent(new SyncBlockEvent(block.getHeader().getHeight() + 1));
		} else {
			logger.warn("Invalid block, height: {}, message: {}", block.getHeader().getHeight(), result.getMessage());
		}
	}

	// new block confirm
	public void newBlock(byte[] body)
	{
		PacketVo packetVo = (PacketVo) SerializeUtils.unSerialize(body);
		Block block = (Block) packetVo.getItem();

		// if confirm failed, remove the block from pool
		if (!packetVo.isSuccess()) {
			logger.info("block confirm failed, {}", packetVo.getMessage());
			blockPool.removeBlock(block.getHeader().getHeight());
		}
	}

	// new message validation
	public void newMessage(byte[] body)
	{
		PacketVo packetVo = (PacketVo) SerializeUtils.unSerialize(body);
		String msgCid = (String) packetVo.getItem();
		if (!packetVo.isSuccess()) {
			logger.error("message {} confirm failed, drop it", msgCid);
			// remove message from message pool
			messagePool.removeMessage(msgCid);
		}
	}
}
