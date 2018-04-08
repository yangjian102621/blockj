package com.aizone.blockchain.core;

import com.aizone.blockchain.encrypt.HashUtils;
import com.google.common.base.Optional;
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
		BlockHeader header = new BlockHeader();
		header.setIndex(blocks.size()+1);
		header.setNonce(100);
		header.setPreviousHash("1");
		header.setDifficulty(BLOCK_DIFFICULT);
		header.setTimestamp(new Date());
		block.setHeader(header);
		block.setHash(HashUtils.sha256(header.toString()));
		block.setBody(new BlockBody());
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
	public Block mining() {
		Integer nonce = miner.proofOfWork(blocks.get(blocks.size()-1));
		Block lastBlock = getLastBlock();
		Block block = new Block();
		//创建区块头
		BlockHeader header = new BlockHeader();
		header.setIndex(blocks.size()+1);
		header.setNonce(nonce);
		header.setDifficulty(BLOCK_DIFFICULT);
		header.setPreviousHash(lastBlock.getHash());
		header.setTimestamp(new Date());
		block.setHash(HashUtils.sha256(header.toString()));
		block.setHeader(header);
		//打包交易
		block.setBody(new BlockBody(getUnPackedTransactions()));
		blocks.add(block);
		return block;
	}

	/**
	 * 发送交易
	 * @param transaction
	 */
	public Transaction sendTransaction(Transaction transaction) {
		String txjHash = HashUtils.sha256(transaction.toString());
		transaction.setTxHash(Optional.of(txjHash));
		unPackedTransactions.add(transaction);
		return transaction;
	}

	/**
	 * 获取最后一个区块
	 * @return
	 */
	public Block getLastBlock() {
		return blocks.get(blocks.size() - 1);
	}
}
