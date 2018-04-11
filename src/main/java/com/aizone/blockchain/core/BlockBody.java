package com.aizone.blockchain.core;

import com.aizone.blockchain.encrypt.HashUtils;
import com.aizone.blockchain.utils.ByteUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 区块数据
 * @author yangjian
 * @since 18-4-8
 */
public class BlockBody implements Serializable {

	/**
	 * 区块所包含的交易记录
	 */
	private List<Transaction> transactions;

	public BlockBody(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public BlockBody() {
		this.transactions = new ArrayList<>();
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	/**
	 * 追加一笔交易
	 * @param transaction
	 */
	public void addTransaction(Transaction transaction) {
		transactions.add(transaction);
	}

	/**
	 * 对区块中的交易信息进行Hash计算
	 *
	 * @return
	 */
	public byte[] hashTransaction() {
		byte[][] txIdArrays = new byte[this.getTransactions().size()][];
		for (int i = 0; i < this.getTransactions().size(); i++) {
			txIdArrays[i] = this.getTransactions().get(i).hash();
		}
		return HashUtils.sha256(ByteUtils.merge(txIdArrays));
	}

}
