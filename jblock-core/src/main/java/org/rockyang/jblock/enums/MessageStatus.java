package org.rockyang.jblock.enums;

/**
 * 交易状态枚举
 * @author yangjian
 * @since 18-4-16
 */
public enum MessageStatus {

	// 交易已确认，交易成功
	SUCCESS("Success", 0),
	// 交易待确认
	APPENDING("Appending", 1),
	INVALID_SIGN("Invalid signature", 2),
	INSUFFICIENT_BALANCE("Insufficient balance", 2),
	FAIL("Invalid Message", -1);

	private final String key;
	private final int value;

	MessageStatus(String key, int value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public int getValue() {
		return value;
	}

	public boolean equals(MessageStatus other) {
		return value == other.value;
	}
}
