package org.rockyang.jblock.chain;

import org.rockyang.jblock.crypto.Hash;
import org.rockyang.jblock.enums.MessageStatus;

import java.math.BigDecimal;

/**
 * block message
 * @author yangjian
 */
public class Message {
	public static final int MSG_VERSION = 1;
	private int version;
	private String from;
	private String to;
	private BigDecimal value;
	private Long timestamp;
	private String pubKey;
	// message CID
	private String cid;
	// massage status
	private MessageStatus status = MessageStatus.APPENDING;
	private int nonce;
	// message parameters
	private String params;
	// block height
	private long height;
	private String sign;

	public Message(String from, String to, BigDecimal value, int nonce) {
		this.from = from;
		this.to = to;
		this.value = value;
		this.timestamp = System.currentTimeMillis();
		this.version = MSG_VERSION;
		this.nonce = nonce;
	}

	public Message() {
		this.timestamp = System.currentTimeMillis();
	}

	public int getVersion()
	{
		return version;
	}

	public void setVersion(int version)
	{
		this.version = version;
	}

	public String getFrom()
	{
		return from;
	}

	public void setFrom(String from)
	{
		this.from = from;
	}

	public String getTo()
	{
		return to;
	}

	public void setTo(String to)
	{
		this.to = to;
	}

	public BigDecimal getValue()
	{
		return value;
	}

	public void setValue(BigDecimal value)
	{
		this.value = value;
	}

	public Long getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(Long timestamp)
	{
		this.timestamp = timestamp;
	}

	public String getPubKey()
	{
		return pubKey;
	}

	public void setPubKey(String pubKey)
	{
		this.pubKey = pubKey;
	}

	public String getCid()
	{
		return cid;
	}

	public void setCid(String cid)
	{
		this.cid = cid;
	}

	public MessageStatus getStatus()
	{
		return status;
	}

	public void setStatus(MessageStatus status)
	{
		this.status = status;
	}

	public int getNonce()
	{
		return nonce;
	}

	public void setNonce(int nonce)
	{
		this.nonce = nonce;
	}

	public String getParams()
	{
		return params;
	}

	public void setParams(String params)
	{
		this.params = params;
	}

	public long getHeight()
	{
		return height;
	}

	public void setHeight(long height)
	{
		this.height = height;
	}

	public String getSign()
	{
		return sign;
	}

	public void setSign(String sign)
	{
		this.sign = sign;
	}

	public String toSigned()
	{
		return "Message{" +
				"from='" + from + '\'' +
				", to='" + to + '\'' +
				", value=" + value +
				", timestamp=" + timestamp +
				", pubKey='" + pubKey + '\'' +
				", cid='" + cid + '\'' +
				", status=" + status +
				", nonce=" + nonce +
				", params='" + params + '\'' +
				'}';
	}

	public String genMsgCid() {
		return Hash.sha3(this.toSigned());
	}

	@Override
	public String toString()
	{
		return "Message{" +
				"version=" + version +
				", from='" + from + '\'' +
				", to='" + to + '\'' +
				", value=" + value +
				", timestamp=" + timestamp +
				", pubKey='" + pubKey + '\'' +
				", cid='" + cid + '\'' +
				", status=" + status +
				", nonce=" + nonce +
				", params='" + params + '\'' +
				", height=" + height +
				", sign='" + sign + '\'' +
				'}';
	}
}
