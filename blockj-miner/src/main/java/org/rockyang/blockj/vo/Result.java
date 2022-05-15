package org.rockyang.blockj.vo;

import java.io.Serializable;

/**
 * @author yangjian
 */
public class Result implements Serializable {

	private boolean ok;
	private String Message;
	public static final Result OK = new Result(true);

	public Result()
	{
	}

	public Result(boolean ok)
	{
		this.ok = ok;
	}

	public Result(boolean ok, String message)
	{
		this.ok = ok;
		Message = message;
	}

	public boolean isOk()
	{
		return ok;
	}

	public void setOk(boolean ok)
	{
		this.ok = ok;
	}

	public String getMessage()
	{
		return Message;
	}

	public void setMessage(String message)
	{
		Message = message;
	}
}
