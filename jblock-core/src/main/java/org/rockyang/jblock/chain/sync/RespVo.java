package org.rockyang.jblock.chain.sync;

/**
 * server response VO
 * @author yangjian
 */
public class RespVo {

	private Object item;
	// operation result
	private boolean success = false;
	// error message
	private String message;

	public RespVo() {
	}

	public RespVo(Object item, boolean status) {
		this.item = item;
		this.success = status;
	}

	public Object getItem() {
		return item;
	}

	public void setItem(Object item) {
		this.item = item;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
