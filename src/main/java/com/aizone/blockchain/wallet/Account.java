package com.aizone.blockchain.wallet;

import com.aizone.blockchain.encrypt.WalletUtils;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 钱包账户
 * @author yangjian
 * @since 18-4-6
 */
public class Account implements Serializable {

	/**
	 * 钱包私钥
	 */
	private String privateKey;

	/**
	 * 钱包公钥
	 */
	private byte[] publicKey;
	/**
	 * 钱包地址
	 */
	private String address;
	/**
	 * 账户余额
	 */
	private BigDecimal balance;

	public Account() {
	}

	public Account(String privateKey, byte[] publicKey) {
		this.privateKey = privateKey;
		this.publicKey = publicKey;
		this.address = WalletUtils.generateAddress(publicKey);
		this.balance = BigDecimal.ZERO;
	}

	public Account(String privateKey, byte[] publicKey, BigDecimal balance) {
		this.privateKey = privateKey;
		this.publicKey = publicKey;
		this.address = WalletUtils.generateAddress(publicKey);
		this.balance = balance;
	}

	public Account(byte[] publicKey) {
		this.publicKey = publicKey;
		this.address = WalletUtils.generateAddress(publicKey);
		this.balance = BigDecimal.ZERO;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public byte[] getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(byte[] publicKey) {
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

	@Override
	public String toString() {
		return "Account{" +
				"privateKey='" + privateKey + '\'' +
				", publicKey=" + WalletUtils.publicKeyEncode(publicKey) +
				", address='" + address + '\'' +
				", balance=" + balance +
				'}';
	}
}
