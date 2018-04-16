package com.aizone.blockchain.core;

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

	@Override
	public String toString() {
		return "BlockBody{" +
				"transactions=" + transactions +
				'}';
	}
}
