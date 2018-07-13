package com.ppblock.blockchain.core;

import com.google.common.base.Objects;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 交易池
 * @author yangjian
 * @since 18-4-23
 */
@Component
public class TransactionPool {

	private List<Transaction> transactions = new ArrayList<>();


	/**
	 * 添加交易
	 * @param transaction
	 */
	public void addTransaction(Transaction transaction) {

		boolean exists = false;
		//检验交易是否存在
		for (Transaction tx : this.transactions) {
			if (Objects.equal(tx.getTxHash(), transaction.getTxHash())) {
				exists = true;
			}
		}
		if (!exists) {
			this.transactions.add(transaction);
		}
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	/**
	 * 清空交易池
	 */
	public void clearTransactions() {
		this.transactions.clear();
	}

}
