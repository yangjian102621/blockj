package com.aizone.blockchain.core;

import com.aizone.blockchain.encrypt.SHAUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 区块链主类
 * @author yangjian
 * @since 18-4-6
 */
@Component
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

	static final BigInteger BLOCK_DIFFICULT = new BigInteger("1000000");

	public Chain() {
		blocks = new ArrayList<>();
		nodes = new ArrayList<>();
		unPackedTransactions = new ArrayList<>();
		//创建创世区块
		Block block = new Block();
		block.setIndex(1);
		block.setNonce(100);
		block.setPreviousHash("1");
		block.setDifficulty(BLOCK_DIFFICULT);
		block.setTimestamp(new Date());
		block.setTransactions(new ArrayList<>());
		blocks.add(block);
	}

	@Autowired
	private Miner miner;

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

	/**
	 * 开始挖矿
	 * @return
	 */
	public Block Mining() {
		Integer nonce = miner.proofOfWork(blocks.get(blocks.size()-1));
		Block prevBlock = blocks.get(blocks.size() - 1);
		Block block = new Block();
		block.setIndex(blocks.size());
		block.setNonce(nonce);
		block.setTransactions(getUnPackedTransactions());
		block.setDifficulty(BLOCK_DIFFICULT);
		block.setPreviousHash(prevBlock.getHash());
		block.setTimestamp(new Date());
		block.setHash(SHAUtils.sha256(block.toString()));
		blocks.add(block);
		return block;
	}

	/**
	 * 发送交易
	 * @param transaction
	 */
	public Transaction sendTransaction(Transaction transaction) {
		String txjHash = SHAUtils.sha256(transaction.toString());
		transaction.setTxHash(txjHash);
		unPackedTransactions.add(transaction);
		return transaction;
	}
}
