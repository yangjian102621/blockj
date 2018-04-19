package com.aizone.blockchain.vo;

import com.aizone.blockchain.core.Transaction;

/**
 * 交易确认 VO
 * @author yangjian
 * @since 2018-04-19 下午10:13.
 */
public class TransactionConfirmVo {

	/**
	 * 待确认交易
	 */
	private Transaction transaction;
	/**
	 * 确认状态
	 */
	private boolean confirmed = false;

	public TransactionConfirmVo() {
	}

	public TransactionConfirmVo(Transaction transaction, boolean confirmed) {
		this.transaction = transaction;
		this.confirmed = confirmed;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}
}
