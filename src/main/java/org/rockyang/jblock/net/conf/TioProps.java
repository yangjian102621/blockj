package org.rockyang.jblock.net.conf;

import org.rockyang.jblock.net.base.Node;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * tio 网络框架配置信息
 * @author yangjian
 * @since 18-4-18
 */
@Component
@ConfigurationProperties("tio")
public class TioProps {

	/**
	 * 心跳包时间间隔
	 */
	@NotNull
	private int heartTimeout;

	/**
	 * 客户端分组名称
	 */
	@NotNull
	private String clientGroupName;

	/**
	 * 服务端分组上下文名称
	 */
	@NotNull
	private String serverGroupContextName;

	/**
	 * 服务端监听端口
	 */
	@NotNull
	private int serverPort;

	/**
	 * 服务端绑定的 ip
	 */
	@NotNull
	private String serverIp;

	@NotNull
	private LinkedHashMap<String, Object> nodes;

	public int getHeartTimeout() {
		return heartTimeout;
	}

	public void setHeartTimeout(int heartTimeout) {
		this.heartTimeout = heartTimeout;
	}

	public String getClientGroupName() {
		return clientGroupName;
	}

	public void setClientGroupName(String clientGroupName) {
		this.clientGroupName = clientGroupName;
	}

	public String getServerGroupContextName() {
		return serverGroupContextName;
	}

	public void setServerGroupContextName(String serverGroupContextName) {
		this.serverGroupContextName = serverGroupContextName;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public List<Node> getNodes() {
		if (null == nodes) {
			return null;
		}
		ArrayList<Node> nodeList = new ArrayList<>();
		Iterator<Map.Entry<String, Object>> iterator= nodes.entrySet().iterator();
		while(iterator.hasNext()) {
			Map.Entry entry = iterator.next();
			Map value = (Map) entry.getValue();
			nodeList.add(new Node(value.get("ip").toString(), Integer.valueOf(value.get("port").toString())));
		}
		return nodeList;
	}

	public void setNodes(LinkedHashMap<String, Object> nodes) {
		this.nodes = nodes;
	}
}
