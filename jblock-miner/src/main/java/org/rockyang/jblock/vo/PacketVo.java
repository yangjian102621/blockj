package org.rockyang.jblock.vo;

/**
 * P2P packet data transfer VO
 * @author yangjian
 */
public class PacketVo {

	private Object item;
	// operation result
	private boolean success = false;
	// error message
	private String message;

	public PacketVo() {
	}

	public PacketVo(Object item, boolean status) {
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
