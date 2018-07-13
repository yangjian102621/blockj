package com.ppblock.blockchain.net.base;

/**
 * 服务器响应 VO
 * @author yangjian
 * @since 2018-04-19 下午10:13.
 */
public class ServerResponseVo {

	/**
	 * 响应实体
	 */
	private Object item;
	/**
	 * 响应状态
	 */
	private boolean success = false;
	/**
	 * 返回错误信息
	 */
	private String message;

	public ServerResponseVo() {
	}

	public ServerResponseVo(Object item, boolean status) {
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
