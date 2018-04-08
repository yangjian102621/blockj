package com.aizone.blockchain.wallet;

import java.math.BigDecimal;

/**
 * 钱包账户
 * @author yangjian
 * @since 18-4-6
 */
public class Account {

	/**
	 * 钱包私钥
	 */
	private String privateKey;
	/**
	 * 钱包公钥
	 */
	private String publicKey;
	/**
	 * 钱包地址
	 */
	private String address;
	/**
	 * 账户余额
	 */
	private BigDecimal balance;

	public Account(String privateKey, String publicKey, String address, BigDecimal balance) {
		this.privateKey = privateKey;
		this.publicKey = publicKey;
		this.address = address;
		this.balance = balance;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
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
