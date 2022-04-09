package org.rockyang.jblock.chain.sync;

import org.rockyang.jblock.chain.Block;
import org.rockyang.jblock.chain.MessagePool;
import org.rockyang.jblock.chain.event.SyncBlockEvent;
import org.rockyang.jblock.chain.service.BlockService;
import org.rockyang.jblock.chain.service.PeerService;
import org.rockyang.jblock.net.ApplicationContextProvider;
import org.rockyang.jblock.net.base.Peer;
import org.rockyang.jblock.net.client.AppClient;
import org.rockyang.jblock.utils.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

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
	private final PeerService peerService;
	private final AppClient client;

	public ClientHandler(BlockService blockService,
	                     MessagePool messagePool,
	                     PeerService peerService,
	                     AppClient client)
	{
		this.blockService = blockService;
		this.messagePool = messagePool;
		this.peerService = peerService;
		this.client = client;
	}

	public void syncBlock(byte[] body)
	{

		RespVo respVo = (RespVo) SerializeUtils.unSerialize(body);
		if (!respVo.isSuccess()) {
			logger.warn("failed to sync block, {}", respVo);
			return;
		}
		Block newBlock = (Block) respVo.getItem();
		Block block = blockService.getBlock(newBlock.getHeader().getHash());
		// keep the older block
		if (block != null && block.getHeader().getTimestamp() <= newBlock.getHeader().getTimestamp()) {
			return;
		}

		if (blockService.checkBlock(newBlock, respVo)) {
			blockService.markBlockAsValidated(newBlock);
			if (block != null) {
				blockService.unmarkBlockAsValidated(block.getHeader().getHash());
			}
			// sync the next block
			ApplicationContextProvider.publishEvent(new SyncBlockEvent(newBlock.getHeader().getHeight() + 1));
		}
	}

	// new block confirm
	public void newBlock(byte[] body)
	{
		RespVo respVo = (RespVo) SerializeUtils.unSerialize(body);
		String blockHash = (String) respVo.getItem();

		if (!respVo.isSuccess() && blockService.isBlockValidated(blockHash)) {
			logger.error("block confirm failed, remove it, {}", blockHash);
			blockService.unmarkBlockAsValidated(blockHash);
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
			messagePool.removeMessage(msgCid);
		}
	}

	// get peers list
	@SuppressWarnings("unchecked")
	public void getPeers(byte[] body) throws Exception
	{
		RespVo respVo = (RespVo) SerializeUtils.unSerialize(body);
		if (!respVo.isSuccess()) {
			return;
		}
		List<Peer> peers = (List<Peer>) respVo.getItem();
		for (Peer peer : peers) {
			if (peerService.hasPeer(peer)) {
				continue;
			}
			client.connect(peer);
		}
	}

	public void newPeer(byte[] body)
	{

	}
}
