package org.rockyang.jblock.base.vo;

/**
 * 返回 Json 字符串 VO
 *
 * @author yangjian
 */
public class JsonVo {

	// version
	private int version = 1;
	// status code
	private CodeEnum code;
	// error message
	private String message;
	private Object data;

	public JsonVo()
	{
	}

	public JsonVo(CodeEnum code, Object data)
	{
		this.code = code;
		this.message = code.getKey();
		this.data = data;
	}

	public JsonVo(CodeEnum code, String message, Object data)
	{
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public JsonVo(CodeEnum code, String message)
	{
		this.code = code;
		this.message = message;
	}

	public static JsonVo success()
	{
		return new JsonVo(CodeEnum.SUCCESS, null);
	}

	public static JsonVo fail()
	{
		return new JsonVo(CodeEnum.FAIL, null);
	}

	public CodeEnum getCode()
	{
		return code;
	}

	public JsonVo setCode(CodeEnum code)
	{
		this.code = code;
		return this;
	}

	public String getMessage()
	{
		return message;
	}

	public JsonVo setMessage(String message)
	{
		this.message = message;
		return this;
	}

	public int getVersion()
	{
		return version;
	}

	public JsonVo setVersion(int version)
	{
		this.version = version;
		return this;
	}

	public Object getData()
	{
		return data;
	}

	public JsonVo setData(Object data)
	{
		this.data = data;
		return this;
	}

	public boolean isOK()
	{
		return code.equals(CodeEnum.SUCCESS);
	}
}
