package org.rockyang.jblock.net.base;

import org.tio.core.ChannelContext;
import org.tio.core.TioConfig;
import org.tio.core.exception.TioDecodeException;
import org.tio.core.intf.Packet;

import java.nio.ByteBuffer;

/**
 * Tio handler 抽象类, 消息编码，解码的通用实现
 *
 * @author yangjian
 */
public abstract class BaseHandler {

	/**
	 * 解码：把接收到的ByteBuffer，解码成应用可以识别的业务消息包
	 * 总的消息结构：消息头 + 消息类别 + 消息体
	 * 消息头结构：    4个字节，存储消息体的长度
	 * 消息类别： 1 个字节， 存储类别，S => 字符串, B => 区块, T => 交易
	 * 消息体结构：   对象的json串的byte[]
	 */
	public MessagePacket decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws TioDecodeException
	{

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
	public ByteBuffer encode(Packet packet, TioConfig config, ChannelContext channelContext)
	{

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

}
