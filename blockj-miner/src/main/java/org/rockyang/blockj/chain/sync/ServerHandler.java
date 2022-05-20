package org.rockyang.blockj.chain.sync;

import org.rockyang.blockj.base.model.Block;
import org.rockyang.blockj.base.model.Message;
import org.rockyang.blockj.base.model.Peer;
import org.rockyang.blockj.base.utils.SerializeUtils;
import org.rockyang.blockj.chain.BlockPool;
import org.rockyang.blockj.chain.MessagePool;
import org.rockyang.blockj.chain.event.NewBlockEvent;
import org.rockyang.blockj.chain.event.NewMessageEvent;
import org.rockyang.blockj.chain.event.NewPeerEvent;
import org.rockyang.blockj.conf.ApplicationContextProvider;
import org.rockyang.blockj.net.base.MessagePacket;
import org.rockyang.blockj.net.base.MessagePacketType;
import org.rockyang.blockj.net.client.P2pClient;
import org.rockyang.blockj.service.BlockService;
import org.rockyang.blockj.service.MessageService;
import org.rockyang.blockj.service.PeerService;
import org.rockyang.blockj.vo.PacketVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tio.core.Node;

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
	private final BlockPool blockPool;
	private final MessageService messageService;
	private final PeerService peerService;
	private final P2pClient client;

	public ServerHandler(BlockService blockService,
	                     MessagePool messagePool,
	                     BlockPool blockPool,
	                     MessageService messageService,
	                     PeerService peerService,
	                     P2pClient client)
	{
		this.blockService = blockService;
		this.messagePool = messagePool;
		this.blockPool = blockPool;
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
		String message = null;
		if (block == null) {
			message = String.format("block not exists, height: %d", height);

		}
		return buildPacket(MessagePacketType.RES_BLOCK_SYNC, block, true, message);
	}

	/**
	 * new block event handler
	 */
	public synchronized MessagePacket newBlock(byte[] body)
	{
		Block block = (Block) SerializeUtils.unSerialize(body);
		logger.info("receive new block confirm request, height: {}", block.getHeader().getHeight());
		if (blockService.isBlockValidated(block.getHeader().getHeight())) {
			logger.info("block exists {}, {}", block.getHeader().getHeight(), block.getHeader().getHash());
			return buildPacket(MessagePacketType.RES_NEW_BLOCK, block, false, "block exists");
		}
		if (!blockPool.hasBlock(block)) {
			// put it to block pool
			blockPool.putBlock(block);

			// if we receive this block for the first time,
			// we need to forward it to the other nodes
			ApplicationContextProvider.publishEvent(new NewBlockEvent(block));
		}
		return buildPacket(MessagePacketType.RES_NEW_BLOCK, block, true, null);
	}

	public synchronized MessagePacket newPeer(byte[] body) throws Exception
	{
		Peer peer = (Peer) SerializeUtils.unSerialize(body);
		if (!peerService.hasPeer(peer)) {
			// store peer
			peerService.addPeer(peer);
		}
		// try to connect peer
		if (client.connect(new Node(peer.getIp(), peer.getPort()))) {
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
