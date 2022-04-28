package org.rockyang.jblock.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author yangjian
 */
@Component
public class MinerConfig {
	@Value("${jblock.repo}")
	private String repo;
	@Value("${jblock.enable-mining}")
	private boolean enabledMining;

	public boolean isEnabledMining()
	{
		return enabledMining;
	}

	public void setEnabledMining(boolean enabledMining)
	{
		this.enabledMining = enabledMining;
	}

	public String getRepo()
	{
		if (repo != null) {
			return repo;
		}
		return System.getProperty("JBLOCK_PATH");
	}

	public void setRepo(String repo)
	{
		this.repo = repo;
	}
}
