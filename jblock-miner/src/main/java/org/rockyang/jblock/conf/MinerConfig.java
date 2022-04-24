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
	@Value("${jblock.enabled-mining}")
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
		String path = System.getProperty("JBLOCK_PATH");
		if (path == null) {
			return System.getProperty("user.home") + "/.jblock";
		}
		return null;
	}

	public void setRepo(String repo)
	{
		this.repo = repo;
	}
}
