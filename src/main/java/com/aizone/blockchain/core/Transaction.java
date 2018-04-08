package com.aizone.blockchain.core;

import com.google.common.base.Optional;

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
	private BigDecimal amount;
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

	public Optional<String> getSender() {
		return Optional.of(sender);
	}

	public void setSender(Optional<String> sender) {
		this.sender = sender.get();
	}

	public String getSign() {
		return sign;
	}

	public void setSign(Optional<String> sign) {
		this.sign = sign.get();
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(Optional<String> recipient) {
		this.recipient = recipient.get();
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(Optional<String> publicKey) {
		this.publicKey = publicKey.get();
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(Optional<BigDecimal> amount) {
		this.amount = amount.get();
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Optional<Date> timestamp) {
		this.timestamp = timestamp.get();
	}

	public String getTxHash() {
		return txHash;
	}

	public void setTxHash(Optional<String> txHash) {
		this.txHash = txHash.get();
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
				", amount=" + amount +
				", timestamp=" + timestamp +
				", txHash='" + txHash + '\'' +
				", data=" + data +
				'}';
	}
}
