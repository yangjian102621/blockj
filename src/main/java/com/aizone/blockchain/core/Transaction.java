package com.aizone.blockchain.core;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易对象
 * @author yangjian
 * @since 18-4-6
 */
public class Transaction {

	/**
	 * 付款人地址
	 */
	private String sender;
	/**
	 * 付款人签名
	 */
	private String sign;
	/**
	 * 收款人地址
	 */
	private String recipient;
	/**
	 * 收款人公钥
	 */
	private String publicKey;
	/**
	 * 交易金额
	 */
	private BigDecimal amout;
	/**
	 * 交易时间戳
	 */
	private Date timestamp;

	/**
	 * 交易 Hash 值
	 */
	private String txHash;
	/**
	 * 附加数据
	 */
	private Serializable data;

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public BigDecimal getAmout() {
		return amout;
	}

	public void setAmout(BigDecimal amout) {
		this.amout = amout;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getTxHash() {
		return txHash;
	}

	public void setTxHash(String txHash) {
		this.txHash = txHash;
	}

	public Serializable getData() {
		return data;
	}

	public void setData(Serializable data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Transaction{" +
				"sender='" + sender + '\'' +
				", recipient='" + recipient + '\'' +
				", publicKey='" + publicKey + '\'' +
				", amout=" + amout +
				", timestamp=" + timestamp +
				", txHash='" + txHash + '\'' +
				", data=" + data +
				'}';
	}
}
