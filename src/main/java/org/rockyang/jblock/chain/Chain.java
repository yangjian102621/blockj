package org.rockyang.jblock.chain;

import com.google.common.base.Preconditions;
import org.rockyang.jblock.crypto.Credentials;
import org.rockyang.jblock.crypto.Sign;
import org.rockyang.jblock.db.Datastore;
import org.rockyang.jblock.enums.MessageStatus;
import org.rockyang.jblock.event.NewBlockEvent;
import org.rockyang.jblock.event.NewTransactionEvent;
import org.rockyang.jblock.miner.Miner;
import org.rockyang.jblock.net.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Optional;

/**
 * @author yangjian
 */
@Component
public class Chain {

	private static final Logger logger = LoggerFactory.getLogger(Chain.class);

	private Datastore datastore;
	private Miner miner;
	private MessagePool messagePool;
	private MessageExecutor messageExecutor;

	public Chain(Datastore datastore, Miner miner, MessagePool messagePool, MessageExecutor messageExecutor)
	{
		this.datastore = datastore;
		this.miner = miner;
		this.messagePool = messagePool;
		this.messageExecutor = messageExecutor;
	}

	public Block mineOne() throws Exception {

		Optional<Block> lastBlock = getLastBlock();
		Block block = miner.mineOne(lastBlock);
		for (Iterator t = messagePool.getTransactions().iterator(); t.hasNext();) {
			block.addMessage((Message) t.next());
			t.remove(); // 已打包的交易移出交易池
		}
//		// 存储区块
//		datastore.chainHead(block.getHeader().getIndex());
//		datastore.putBlock(block);
		logger.info("Find a New Block, {}", block);
		ApplicationContextProvider.publishEvent(new NewBlockEvent(block));
		return block;
	}

	/**
	 * 发送交易
	 * @param credentials 交易发起者的凭证
	 * @param to 交易接收者
	 * @param amount
	 * @param data 交易附言
	 * @return
	 * @throws Exception
	 */
	public Message sendTransaction(Credentials credentials, String to, BigDecimal amount, String data) throws
			Exception {

		//校验付款和收款地址
		Preconditions.checkArgument(to.startsWith("0x"), "收款地址格式不正确");
		Preconditions.checkArgument(!credentials.getAddress().equals(to), "收款地址不能和发送地址相同");

		//构建交易对象
		Message message = new Message(credentials.getAddress(), to, amount);
		message.setStatus(MessageStatus.APPENDING);
		message.setParams(data);
		message.setCid(message.genMsgCid());
		//签名
		String sign = Sign.sign(credentials.getEcKeyPair().getPrivateKey(), message.toSigned());
		message.setSign(sign);

		//先验证私钥是否正确
		if (!Sign.verify(credentials.getEcKeyPair().getPublicKey(), sign, message.toSigned())) {
			throw new RuntimeException("私钥签名验证失败，非法的私钥");
		}
		// 加入交易池，等待打包
		messagePool.pendingMessage(message);

		ApplicationContextProvider.publishEvent(new NewTransactionEvent(message));
		return message;
	}

	/**
	 * 获取最后一个区块
	 * @return
	 */
	public Optional<Block> getLastBlock() {
//		return dataStore.getLastBlock();
		return Optional.empty();
	}

	/**
	 * 添加一个节点
	 * @param ip
	 * @param port
	 * @return
	 */
	public void addNode(String ip, int port) throws Exception {

//		appClient.addNode(ip, port);
//		Node node = new Node(ip, port);
//		dataStore.addNode(node);
	}
}
