package org.rockyang.blockchain.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 系统全局配置
 * @author yangjian
 * @since 18-7-14
 */
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConf {

	/**
	 * 是否启用节点发现
	 */
	private boolean nodeDiscover;

	/**
	 * 是否自动挖矿
	 */
	private boolean autoMining;

	/**
	 * 数据存储地址
	 */
	private String dataDir;

	public boolean isNodeDiscover() {
		return nodeDiscover;
	}

	public void setNodeDiscover(boolean nodeDiscover) {
		this.nodeDiscover = nodeDiscover;
	}

	public boolean isAutoMining() {
		return autoMining;
	}

	public void setAutoMining(boolean autoMining) {
		this.autoMining = autoMining;
	}

	public String getDataDir() {
		return dataDir;
	}

	public void setDataDir(String dataDir) {
		this.dataDir = dataDir;
	}
}
