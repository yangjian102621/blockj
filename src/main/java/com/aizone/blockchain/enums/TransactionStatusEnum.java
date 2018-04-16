package com.aizone.blockchain.enums;

/**
 * 交易状态枚举
 * @author yangjian
 * @since 18-4-16
 */
public enum  TransactionStatusEnum {

	FAIL(0, "交易失败"),
	SUCCESS(1, "交易成功");

	private int value;
	private String name;

	TransactionStatusEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
