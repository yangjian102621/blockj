package com.ppblock.blockchain.account;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 钱包账户
 * @author yangjian
 * @since 18-4-6
 */
public class Account implements Serializable {

	/**
	 * 钱包地址
	 */
	private String address;

	/**
	 * 账户余额
	 */
	private BigDecimal balance;

	public Account() {}

	public Account(String address, BigDecimal balance) {
		this.address = address;
		this.balance = balance;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
}
