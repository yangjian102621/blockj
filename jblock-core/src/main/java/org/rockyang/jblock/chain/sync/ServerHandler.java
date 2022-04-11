package org.rockyang.jblock.chain.sync;

import org.rockyang.jblock.chain.Block;
import org.rockyang.jblock.chain.Message;
import org.rockyang.jblock.chain.MessagePool;
import org.rockyang.jblock.chain.event.NewBlockEvent;
import org.rockyang.jblock.chain.event.NewMessageEvent;
import org.rockyang.jblock.chain.event.NewPeerEvent;
import org.rockyang.jblock.chain.service.BlockService;
import org.rockyang.jblock.chain.service.MessageService;
import org.rockyang.jblock.chain.service.PeerService;
import org.rockyang.jblock.net.ApplicationContextProvider;
import org.rockyang.jblock.net.base.MessagePacket;
import org.rockyang.jblock.net.base.MessagePacketType;
import org.rockyang.jblock.net.base.Peer;
import org.rockyang.jblock.net.client.AppClient;
import org.rockyang.jblock.utils.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * message response handler
 * reply the message that send from the other client
 *
 * @author yangjian
 */
@Component
public class ServerHandler {

	private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

	private final BlockService blockService;
	private final MessagePool messagePool;
	private final MessageService messageService;
	private final PeerService peerService;
	private final AppClient client;

	public ServerHandler(BlockService blockService,
	                     MessagePool messagePool,
	                     MessageService messageService,
	                     PeerService peerService,
	                     AppClient client)
	{
		this.blockService = blockService;
		this.messagePool = messagePool;
		this.messageService = messageService;
		this.peerService = peerService;
		this.client = client;
	}

	// new message validation
	public MessagePacket newMessage(byte[] body)
	{
		RespVo respVo = new RespVo();
		MessagePacket resPacket = new MessagePacket();
		Message message = (Message) SerializeUtils.unSerialize(body);
		logger.info("receive a new message， {}", message);
		respVo.setItem(message.getCid());
		// validate the message
		if (!messagePool.hasMessage(message) && messageService.validateMessage(message)) {
			respVo.setSuccess(true);
			// put message into message pool
			messagePool.pendingMessage(message);
			// broadcast message to other peers
			ApplicationContextProvider.publishEvent(new NewMessageEvent(message));
		} else {
			respVo.setSuccess(false);
			respVo.setMessage("Invalid message signature");
			logger.info("failed to validate the message, invalid signature, {}", message);
		}
		resPacket.setType(MessagePacketType.RES_NEW_MESSAGE);
		resPacket.setBody(SerializeUtils.serialize(respVo));

		return resPacket;
	}

	public MessagePacket syncBlock(byte[] body)
	{
		RespVo respVo = new RespVo();
		MessagePacket resPacket = new MessagePacket();
		long height = (long) SerializeUtils.unSerialize(body);
		logger.info("receive a block sync request, height: {}", height);
		Block block = blockService.getBlockByHeight(height);
		if (block != null) {
			respVo.setItem(block);
			respVo.setSuccess(true);
		} else {
			respVo.setSuccess(false);
			respVo.setItem(null);
			respVo.setMessage(String.format("block not exists, height: %d", height));
		}
		resPacket.setType(MessagePacketType.RES_BLOCK_SYNC);
		resPacket.setBody(SerializeUtils.serialize(respVo));
		return resPacket;
	}

	/**
	 * new block event handler
	 */
	public MessagePacket newBlock(byte[] body)
	{
		RespVo respVo = new RespVo();
		MessagePacket resPacket = new MessagePacket();
		Block newBlock = (Block) SerializeUtils.unSerialize(body);
		logger.info("receive new block confirm request： {}", newBlock);
		if (blockService.checkBlock(newBlock, respVo)) {
			respVo.setSuccess(true);
			if (!blockService.isBlockValidated(newBlock.getHeader().getHash())) {
				blockService.markBlockAsValidated(newBlock);
				logger.info("block confirmation successfully, height: {}, hash：{}", newBlock.getHeader().getHeight(), newBlock.getHeader().getHash());
				// broadcast block to other peers
				ApplicationContextProvider.publishEvent(new NewBlockEvent(newBlock));
			}
		} else {
			respVo.setSuccess(false);
			logger.error("block confirmation failed：{}", respVo.getMessage());
		}
		respVo.setItem(newBlock.getHeader().getHash());
		resPacket.setType(MessagePacketType.RES_NEW_BLOCK);
		resPacket.setBody(SerializeUtils.serialize(respVo));

		return resPacket;
	}

	public MessagePacket newPeer(byte[] body) throws Exception
	{
		Peer peer = (Peer) SerializeUtils.unSerialize(body);
		if (!peerService.hasPeer(peer)) {
			// store peer
			peerService.addPeer(peer);
			// try to connect peer
			client.connect(peer);
		}
		// fire new peer connected event
		ApplicationContextProvider.publishEvent(new NewPeerEvent(peer));
		return null;
	}
}
