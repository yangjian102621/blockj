package com.aizone.blockchain.core;

import com.aizone.blockchain.db.DBAccess;
import com.aizone.blockchain.encrypt.HashUtils;
import com.aizone.blockchain.mine.Miner;
import com.aizone.blockchain.utils.HttpUtils;
import com.aizone.blockchain.utils.JsonVo;
import com.aizone.blockchain.wallet.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
	Miner miner;

	@Autowired
	DBAccess dbAccess;

	/**
	 * 节点列表
	 */
	private List<String> nodes;
	/**
	 * 未打包的交易列表
	 */
	private List<Transaction> unPackedTransactions;

	public BlockChain() {
		this.unPackedTransactions = new ArrayList<>();
		this.nodes = new ArrayList<>();
	}

	public List<String> getNodes() {
		return nodes;
	}

	public void setNodes(List<String> nodes) {
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
		//存储区块
		dbAccess.putBlock(block);
		logger.info("Find a New Block, {}", block);
		return block;
	}

	/**
	 * 发送交易
	 * @param transaction
	 * @param privateKey 付款人私钥，用来签名交易
	 */
	public Transaction sendTransaction(Transaction transaction, String privateKey) {

		transaction.setTxHash(HashUtils.sha256Hex(transaction.toString()));
		//从数据库查询到用户的公钥
		Optional<Account> account = dbAccess.getAccount(transaction.getRecipient());
		if (account.isPresent()) {
			throw new RuntimeException("收款人地址不存在");
		}
		transaction.setPublicKey(account.get().getPublicKey());
		//打包数据到待挖区块
		this.unPackedTransactions.add(transaction);
		return transaction;
	}

	/**
	 * 获取最后一个区块
	 * @return
	 */
	public Optional<Block> getLastBlock() {
		return dbAccess.getLastBlock();
	}

	/**
	 * 添加一个节点
	 * @param node
	 * @return
	 */
	public boolean addNode(String node) {

		//检测是否是一个有效的节点地址，使用 ping 测试节点的状态
		HashMap<String, Object> map = new HashMap<>(1);
		try {
			String s = HttpUtils.get(node + "/chain/ping", map);
			JsonVo vo = OBJECT_MAPPER.readValue(s, JsonVo.class);
			if (vo.getCode() == JsonVo.CODE_SUCCESS) {
				nodes.add(node);
				return true;
			}
		} catch (IOException e) {
			return false;
		}
		return false;
	}
}
