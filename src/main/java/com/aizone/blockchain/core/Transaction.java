package com.aizone.blockchain.core;

import com.aizone.blockchain.encrypt.HashUtils;
import com.aizone.blockchain.encrypt.WalletUtils;
import com.aizone.blockchain.utils.SerializeUtils;

import java.io.Serializable;
import java.math.BigDecimal;

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
	private byte[] publicKey;
	/**
	 * 交易金额
	 */
	private BigDecimal amount;
	/**
	 * 交易时间戳
	 */
	private long timestamp;

	/**
	 * 交易 Hash 值
	 */
	private String txHash;
	/**
	 * 附加数据
	 */
	private Serializable data;

	public Transaction(String sender, String recipient, BigDecimal amount) {
		this.sender = sender;
		this.recipient = recipient;
		this.amount = amount;
	}

	public Transaction() {
	}

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

	public byte[] getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(byte[] publicKey) {
		this.publicKey = publicKey;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
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

	/**
	 * 计算交易信息的Hash值
	 * @return
	 */
	public byte[] hash() {
		// 使用序列化的方式对Transaction对象进行深度复制
		byte[] serializeBytes = SerializeUtils.serialize(this);
		Transaction copyTx = (Transaction) SerializeUtils.unSerialize(serializeBytes);
		return HashUtils.sha256(SerializeUtils.serialize(copyTx));
	}

	@Override
	public String toString() {
		return "Transaction{" +
				"sender='" + sender + '\'' +
				", recipient='" + recipient + '\'' +
				", publicKey='" + WalletUtils.publicKeyEncode(publicKey) + '\'' +
				", amount=" + amount +
				", timestamp=" + timestamp +
				", txHash='" + txHash + '\'' +
				", data=" + data +
				'}';
	}
}
