package com.ppblock.blockchain.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * RocksDB 配置参数
 * @author yangjian
 * @since 2018-04-21 下午4:14.
 */
@Configuration
@ConfigurationProperties("rocksdb")
public class RocksDbProperties {

	private String dataDir;

	public String getDataDir() {
		return dataDir;
	}

	public void setDataDir(String dataDir) {
		this.dataDir = dataDir;
	}
}
