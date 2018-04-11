package com.aizone.blockchain.core;

import com.aizone.blockchain.utils.RocksDBUtils;
import com.google.common.base.Optional;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 区块链主类
 * @author yangjian
 * @since 18-4-6
 */
@Component
public class BlockChain {



	/**
	 * 最后一个区块的 hash 值
	 */
	private String lastBlockHash;
	/**
	 * 节点列表
	 */
	private List<String> nodes;
	/**
	 * 未打包的交易列表
	 */
	private List<Transaction> unPackedTransactions;

	/**
	 * 创建区块链实例
	 * @return
	 * @throws Exception
	 */
	public static BlockChain newInstance() throws Exception {
		Optional<String> lastBlockHash = RocksDBUtils.getInstance().getLastBlockHash();
		if (lastBlockHash.isPresent()) {
			return new BlockChain(lastBlockHash.get());
		} else {
			BlockChain chain = new BlockChain();
			//创建创世区块
			return chain;
		}
	}

	public BlockChain() {
	}

	public BlockChain(String lastBlockHash) {
		this.lastBlockHash = lastBlockHash;
	}

	public String getLastBlockHash() {
		return lastBlockHash;
	}

	public void setLastBlockHash(String lastBlockHash) {
		this.lastBlockHash = lastBlockHash;
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
	 * 开始挖矿
	 * @return
	 */
	public Block mining() {
		return null;
	}

	/**
	 * 发送交易
	 * @param transaction
	 */
	public Transaction sendTransaction(Transaction transaction) {
		return null;
	}

	/**
	 * 获取最后一个区块
	 * @return
	 */
	public Block getLastBlock() {
		return null;
	}
}
