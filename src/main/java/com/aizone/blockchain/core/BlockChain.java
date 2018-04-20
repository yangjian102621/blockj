package com.aizone.blockchain.core;

import com.aizone.blockchain.db.DBUtils;
import com.aizone.blockchain.encrypt.HashUtils;
import com.aizone.blockchain.encrypt.SignUtils;
import com.aizone.blockchain.enums.TransactionStatusEnum;
import com.aizone.blockchain.event.MineBlockEvent;
import com.aizone.blockchain.event.SendTransactionEvent;
import com.aizone.blockchain.mine.Miner;
import com.aizone.blockchain.net.ApplicationContextProvider;
import com.aizone.blockchain.net.client.AppClient;
import com.aizone.blockchain.wallet.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tio.core.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * 区块链主类
 * @author yangjian
 * @since 18-4-6
 */
@Component
public class BlockChain {

	static Logger logger = LoggerFactory.getLogger(BlockChain.class);
	/**
	 * json 处理工具
	 */
	static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	@Autowired
	AppClient appClient;

	@Autowired
	Miner miner;

	/**
	 * 区块链节点列表
	 */
	private List<Node> nodes;
	/**
	 * 未打包的交易列表
	 */
	private List<Transaction> unPackedTransactions;

	public BlockChain() {
		this.unPackedTransactions = new ArrayList<>();
		this.nodes = new ArrayList<>();
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	public List<Transaction> getUnPackedTransactions() {
		return unPackedTransactions;
	}

	public void setUnPackedTransactions(List<Transaction> unPackedTransactions) {
		this.unPackedTransactions = unPackedTransactions;
	}

	/**
	 * 挖取一个区块
	 * @return
	 */
	public Block mining() throws Exception {

		Optional<Block> lastBlock = getLastBlock();
		Block block = miner.newBlock(lastBlock);
		this.getUnPackedTransactions().forEach(e -> block.getBody().addTransaction(e));
		for (Transaction transaction : block.getBody().getTransactions()) {
			synchronized (this) {

				Optional<Account> recipient = DBUtils.getAccount(transaction.getRecipient());
				//挖矿奖励
				if (null == transaction.getSender()) {
					recipient.get().setBalance(recipient.get().getBalance().add(transaction.getAmount()));
					DBUtils.putAccount(recipient.get());
					continue;
				}
				//账户转账
				Optional<Account> sender = DBUtils.getAccount(transaction.getSender());
				//验证签名
				boolean verify = SignUtils.verify(sender.get().getPublicKey(), transaction.getSign(), transaction.toString());
				if (!verify) {
					transaction.setStatus(TransactionStatusEnum.FAIL);
					transaction.setErrorMessage("交易签名错误");
					continue;
				}
				//验证账户余额
				if (sender.get().getBalance().compareTo(transaction.getAmount()) == -1) {
					transaction.setStatus(TransactionStatusEnum.FAIL);
					transaction.setErrorMessage("账户余额不足");
					continue;
				}

				//执行转账操作
				sender.get().setBalance(sender.get().getBalance().subtract(transaction.getAmount()));
				recipient.get().setBalance(recipient.get().getBalance().add(transaction.getAmount()));
				DBUtils.putAccount(sender.get());
				DBUtils.putAccount(recipient.get());
			}
		}
		//清空待打包交易
		this.unPackedTransactions.clear();
		//存储区块
		DBUtils.putBlock(block);
		logger.info("Find a New Block, {}", block);

		//触发挖矿事件，并等待其他节点确认区块
		ApplicationContextProvider.publishEvent(new MineBlockEvent(block));
		return block;
	}

	/**
	 * 发送交易
	 * @param transaction
	 * @param privateKey 付款人私钥，用来签名交易
	 */
	public Transaction sendTransaction(Transaction transaction, String privateKey) throws Exception {

		//从数据库查询到用户的公钥
		Optional<Account> sender = DBUtils.getAccount(transaction.getSender());
		Optional<Account> recipient = DBUtils.getAccount(transaction.getRecipient());
		if (!sender.isPresent()) {
			throw new RuntimeException("付款人地址不存在");
		}
		if (!recipient.isPresent()) {
			throw new RuntimeException("收款人地址不存在");
		}
		transaction.setPublicKey(sender.get().getPublicKey());
		transaction.setTxHash(HashUtils.sha256Hex(transaction.toString()));
		//签名
		String sign = SignUtils.sign(privateKey, transaction.toString());
		transaction.setSign(sign);
		//打包数据到待挖区块
		this.unPackedTransactions.add(transaction);

		//触发交易事件，向全网广播交易，并等待确认
		ApplicationContextProvider.publishEvent(new SendTransactionEvent(transaction));
		return transaction;
	}

	/**
	 * 获取最后一个区块
	 * @return
	 */
	public Optional<Block> getLastBlock() {
		return DBUtils.getLastBlock();
	}

	/**
	 * 添加一个节点
	 * @param ip
	 * @param port
	 * @return
	 */
	public boolean addNode(String ip, int port) {

		Node node = new Node(ip, port);

		return false;
	}
}
