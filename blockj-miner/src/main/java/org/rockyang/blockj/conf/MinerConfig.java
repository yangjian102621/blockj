package org.rockyang.blockj.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author yangjian
 */
@Component
public class MinerConfig {
	@Value("${blockj.repo}")
	private String repo;
	@Value("${blockj.enable-mining}")
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
		return System.getProperty("blockj_PATH");
	}

	public void setRepo(String repo)
	{
		this.repo = repo;
	}
}
