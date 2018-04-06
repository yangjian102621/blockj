package com.aizone.blockchain.core;

import java.util.List;

/**
 * 区块链主类
 * @author yangjian
 * @since 18-4-6
 */
public class Chain {

	/**
	 * 区块集合
	 */
	private List<Block> blocks;
	/**
	 * 节点列表
	 */
	private List<String> nodes;
	/**
	 * 未打包的交易列表
	 */
	private List<Transaction> unPackedTransactions;

	public List<Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<Block> blocks) {
		this.blocks = blocks;
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
}
