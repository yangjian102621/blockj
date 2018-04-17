package com.aizone.blockchain.net.base;

/**
 * 网络地址常量
 * @author yangjian
 */
public interface Const {
	/**
	 * 服务器地址
	 */
	String SERVER = "127.0.0.1";
	
	/**
	 * 监听端口
	 */
	int PORT = 6789;

	/**
	 * 心跳超时时间
	 */
	int TIMEOUT = 5000;

	/**
	 * 客户端分组名称
	 */
	String GROUP_NAME = "test-group";
}
