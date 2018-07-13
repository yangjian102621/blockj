package com.ppblock.blockchain.utils;

/**
 * 返回 Json 字符串 VO
 * @author yangjian
 * @since 2018-04-07 上午10:56.
 */
public class JsonVo {

	public static final int CODE_SUCCESS = 200;
	public static final int CODE_FAIL = 400;

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

	public JsonVo(int code, String message, Object item) {
		this.code = code;
		this.message = message;
		this.item = item;
	}

	public static JsonVo instance(int code, String message) {
		return new JsonVo(code, message);
	}

	public JsonVo(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public JsonVo(int code, Object item) {
		this.code = code;
		this.item = item;
	}

	public static JsonVo success() {
		return new JsonVo(CODE_SUCCESS, "SUCCESS", null);
	}

	public static JsonVo fail() {
		return new JsonVo(CODE_FAIL, "FAIL", null);
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
