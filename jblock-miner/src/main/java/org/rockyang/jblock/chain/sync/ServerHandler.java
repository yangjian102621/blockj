package org.rockyang.jblock.chain.sync;

import org.rockyang.jblock.base.model.Block;
import org.rockyang.jblock.base.model.Message;
import org.rockyang.jblock.base.utils.SerializeUtils;
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
import org.rockyang.jblock.vo.PacketVo;
import org.rockyang.jblock.vo.Result;
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
	public synchronized MessagePacket newMessage(byte[] body)
	{
		PacketVo packetVo = new PacketVo();
		MessagePacket resPacket = new MessagePacket();
		Message message = (Message) SerializeUtils.unSerialize(body);
		logger.info("receive a new message， {}", message);

		packetVo.setItem(message.getCid());
		// validate the message
		if (!messagePool.hasMessage(message) && messageService.validateMessage(message)) {
			packetVo.setSuccess(true);
			// put message into message pool
			messagePool.pendingMessage(message);
			// broadcast message to other peers
			ApplicationContextProvider.publishEvent(new NewMessageEvent(message));
			return buildPacket(MessagePacketType.RES_NEW_MESSAGE, message.getCid(), true, null);
		} else {
			logger.info("failed to validate the message, invalid signature, {}", message);
			return buildPacket(MessagePacketType.RES_NEW_MESSAGE, message.getCid(), false, "Invalid message signature");
		}
	}

	public synchronized MessagePacket syncBlock(byte[] body)
	{
		long height = (long) SerializeUtils.unSerialize(body);
		logger.info("receive a block sync request, height: {}", height);
		Block block = blockService.getBlockByHeight(height);
		if (block == null) {
			String message = String.format("block not exists, height: %d", height);
			return buildPacket(MessagePacketType.RES_BLOCK_SYNC, null, false, message);
		} else {
			return buildPacket(MessagePacketType.RES_BLOCK_SYNC, block, true, null);
		}
	}

	/**
	 * new block event handler
	 */
	public synchronized MessagePacket newBlock(byte[] body) throws Exception
	{
		Block block = (Block) SerializeUtils.unSerialize(body);
		logger.info("receive new block confirm request, height: {}, hash: {}", block.getHeader().getHeight(), block.getHeader().getHash());
		if (blockService.isBlockValidated(block)) {
			logger.info("block exists {}, {}", block.getHeader().getHeight(), block.getHeader().getHash());
			return buildPacket(MessagePacketType.RES_NEW_BLOCK, block.getHeader().getHash(), false, null);
		}
		Result result = blockService.checkBlock(block);
		if (result.isOk()) {
			blockService.markBlockAsValidated(block);
			logger.info("block validate successfully, height: {}, hash：{}", block.getHeader().getHeight(), block.getHeader().getHash());

			// if we receive this block for the first time,
			// we need to forward it to the other nodes
			ApplicationContextProvider.publishEvent(new NewBlockEvent(block));
			return buildPacket(MessagePacketType.RES_NEW_BLOCK, block.getHeader().getHash(), true, null);
		} else {
			return buildPacket(MessagePacketType.RES_NEW_BLOCK, block.getHeader().getHash(), false, result.getMessage());
		}
	}

	public synchronized MessagePacket newPeer(byte[] body) throws Exception
	{
		Peer peer = (Peer) SerializeUtils.unSerialize(body);
		if (!peerService.hasPeer(peer)) {
			// store peer
			peerService.addPeer(peer);
		}
		// try to connect peer
		if (client.connect(peer)) {
			// fire new peer connected event
			ApplicationContextProvider.publishEvent(new NewPeerEvent(peer));
		}
		return null;
	}

	private MessagePacket buildPacket(byte type, Object data, boolean status, String message)
	{
		MessagePacket resPacket = new MessagePacket();
		PacketVo packetVo = new PacketVo(data, status);
		packetVo.setMessage(message);
		resPacket.setType(type);
		resPacket.setBody(SerializeUtils.serialize(packetVo));
		return resPacket;
	}
}
