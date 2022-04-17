package org.rockyang.jblock.base.enums;

/**
 * Message methods enum
 *
 * @author yangjian
 */
public enum MessageMethod {

	// transfer coins
	SEND("MethodSend", 0);

	private final String key;
	private final int value;

	MessageMethod(String key, int value)
	{
		this.key = key;
		this.value = value;
	}

	public String getKey()
	{
		return key;
	}

	public int getValue()
	{
		return value;
	}
}
