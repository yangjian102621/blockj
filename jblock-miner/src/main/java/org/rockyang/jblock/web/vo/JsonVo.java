package org.rockyang.jblock.web.vo;

/**
 * 返回 Json 字符串 VO
 * @author yangjian
 */
public class JsonVo {

	// version
	private int version;
	// status code
	private CodeEnum code;
	// error message
	private String message;
	private Object data;

	public JsonVo() {}

	public JsonVo(CodeEnum code, Object data) {
		this.code = code;
		this.message = code.getKey();
		this.data = data;
	}

	public JsonVo(CodeEnum code, String message, Object data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public JsonVo(CodeEnum code, String message) {
		this.code = code;
		this.message = message;
	}

	public static JsonVo success() {
		return new JsonVo(CodeEnum.SUCCESS, null);
	}

	public static JsonVo fail() {
		return new JsonVo(CodeEnum.FAIL, null);
	}

	public CodeEnum getCode() {
		return code;
	}

	public void setCode(CodeEnum code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


}
