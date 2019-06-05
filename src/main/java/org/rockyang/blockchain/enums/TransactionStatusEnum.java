package org.rockyang.blockchain.enums;

/**
 * 交易状态枚举
 * @author yangjian
 * @since 18-4-16
 */
public enum  TransactionStatusEnum {

	// 交易已确认，交易成功
	SUCCESS("Success", 1),
	// 交易待确认
	APPENDING("Appending", 0),
	// 交易确认失败
	FAIL("Fail", -1);

	private String key;
	private int value;

	TransactionStatusEnum(String key, int value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
