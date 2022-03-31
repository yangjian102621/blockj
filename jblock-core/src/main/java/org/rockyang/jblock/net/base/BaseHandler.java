package org.rockyang.jblock.net.base;

import org.rockyang.jblock.chain.Block;
import org.rockyang.jblock.store.Datastore;
import org.tio.core.ChannelContext;
import org.tio.core.TioConfig;
import org.tio.core.exception.TioDecodeException;
import org.tio.core.intf.Packet;

import java.nio.ByteBuffer;

/**
 * 抽象的  AioHandler, 消息编码，解码的通用实现
 * @author yangjian
 * @since 18-4-17
 */
public abstract class BaseHandler {

	public static MessagePacket heartbeatPacket = new MessagePacket(MessagePacketType.STRING_MESSAGE);
	/**
	 * 解码：把接收到的ByteBuffer，解码成应用可以识别的业务消息包
	 * 总的消息结构：消息头 + 消息类别 + 消息体
	 * 消息头结构：    4个字节，存储消息体的长度
	 * 消息类别： 1 个字节， 存储类别，S => 字符串, B => 区块, T => 交易
	 * 消息体结构：   对象的json串的byte[]
	 */
	public MessagePacket decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws TioDecodeException {

		//收到的数据组不了业务包，则返回null以告诉框架数据不够
		if (readableLength < MessagePacket.HEADER_LENGTH) {
			return null;
		}
		//读取消息类别
		byte messageType = buffer.get();
		//读取消息体的长度
		int bodyLength = buffer.getInt();

		//数据不正确，则抛出AioDecodeException异常
		if (bodyLength < 0) {
			throw new TioDecodeException("bodyLength [" + bodyLength + "] is not right, remote:" + channelContext.getClientNode());
		}
		//计算本次需要的数据长度
		int neededLength = MessagePacket.HEADER_LENGTH + bodyLength;
		//收到的数据是否足够组包
		int isDataEnough = readableLength - neededLength;
		// 不够消息体长度(剩下的buffe组不了消息体)
		if (isDataEnough < 0) {
			return null;
		} else //组包成功
		{
			MessagePacket imPacket = new MessagePacket();
			imPacket.setType(messageType);
			if (bodyLength > 0) {
				byte[] dst = new byte[bodyLength];
				buffer.get(dst);
				imPacket.setBody(dst);
			}
			return imPacket;
		}
	}

	/**
	 * 编码：把业务消息包编码为可以发送的ByteBuffer
	 * 总的消息结构：消息头 + 消息类别 + 消息体
	 * 消息头结构：    4个字节，存储消息体的长度
	 * 消息类别： 1 个字节， 存储类别，S => 字符串, B => 区块, T => 交易
	 * 消息体结构：   对象的json串的byte[]
	 */
	public ByteBuffer encode(Packet packet, TioConfig config, ChannelContext channelContext) {

		MessagePacket messagePacket = (MessagePacket) packet;
		byte[] body = messagePacket.getBody();
		int bodyLen = 0;
		if (body != null) {
			bodyLen = body.length;
		}

		//bytebuffer的总长度是 = 消息头的长度 + 消息体的长度
		int allLen = MessagePacket.HEADER_LENGTH + bodyLen;
		//创建一个新的bytebuffer
		ByteBuffer buffer = ByteBuffer.allocate(allLen);
		//设置字节序
		buffer.order(config.getByteOrder());

		//写入消息类型
		buffer.put(messagePacket.getType());
		//写入消息头----消息头的内容就是消息体的长度
		buffer.putInt(bodyLen);

		//写入消息体
		if (body != null) {
			buffer.put(body);
		}
		return buffer;
	}

	/**
	 * 检验区块是否合法
	 * 1. 验证改区块前一个区块是否存在，且 previousHash 是否合法（暂时不做验证）
	 * 2. 验证该区块本身 hash 是否合法
	 * @param block
	 * @param dataStore
	 * @return
	 */
	public boolean checkBlock(Block block, Datastore dataStore) {

		//创世区块
//		if (block.getHeight() == 1) {
//			return Objects.equal(block.getHash(), block.genBlockHash());
//		}
//
//		boolean blockValidate = false;
////		if (block.getHeader().getIndex() > 1) {
////			Optional<Block> prevBlock = dataStore.getBlock(block.getHeader().getIndex()-1);
////			if (prevBlock.isPresent()
////					&& prevBlock.get().getHeader().getHash().equals(block.getHeader().getPreviousHash())) {
////				blockValidate = true;
////			}
////		}
//		//检查是否符合工作量证明
//		ProofOfWork proofOfWork = ProofOfWork.newProofOfWork(block);
//		if (!proofOfWork.validate()) {
//			blockValidate = false;
//		}
//
//		return blockValidate;
		return true;
	}


	public Packet heartbeatPacket(ChannelContext context) {
		return heartbeatPacket;
	}

}
