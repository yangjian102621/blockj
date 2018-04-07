package com.aizone.blockchain.utils;

/**
 * 返回 Json 字符串 VO
 * @author yangjian
 * @since 2018-04-07 上午10:56.
 */
public class JsonVo {

	/**
	 * 返回状态码
	 */
	private int code;
	/**
	 * 返回错误信息
	 */
	private String message;
	/**
	 * 返回数据
	 */
	private Object item;

	public JsonVo() {}

	public JsonVo(int code, String message, Class<?> item) {
		this.code = code;
		this.message = message;
		this.item = item;
	}

	public static JsonVo success() {
		return new JsonVo(200, "SUCCESS", null);
	}

	public static JsonVo fail() {
		return new JsonVo(400, "FAIL", null);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getItem() {
		return item;
	}

	public void setItem(Object item) {
		this.item = item;
	}
}
