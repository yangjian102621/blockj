package com.aizone.blockchain.web.vo;

import java.math.BigDecimal;

/**
 * 发送交易参数 VO
 * @author yangjian
 * @since 18-4-13
 */
public class TransactionVo {

	/**
	 * 付款人地址
	 */
	private String sender;
	/**
	 * 收款人地址
	 */
	private String recipient;
	/**
	 * 交易金额
	 */
	private BigDecimal amount;
	/**
	 * 付款人私钥
	 */
	private String privateKey;
	/**
	 * 附加数据
	 */
	private String data;

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
