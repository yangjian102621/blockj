package org.rockyang.jblock.web.vo;

/**
 * @author yangjian
 */
public enum CodeEnum {
	SUCCESS("Success", 200),
	INVALID_ARGS("Invalid arguments", 101),
	FAIL("Failed", 400);

	private String key;
	private int value;

	CodeEnum(String key, int value)
	{
		this.key = key;
		this.value = value;
	}

	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	public int getValue()
	{
		return value;
	}

	public void setValue(int value)
	{
		this.value = value;
	}
}
