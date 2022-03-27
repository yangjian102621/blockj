package org.rockyang.jblock.conf;

import org.springframework.stereotype.Component;

/**
 * @author yangjian
 */
@Component
public class MinerConfig {

	private String repo;

	public String getRepo()
	{
		if (repo != null) {
			return repo;
		}
		String path = System.getProperty("JBLOCK_PATH");
		if (path == null) {
			return System.getProperty("user.home")+"/.jblock";
		}
		return null;
	}

	public void setRepo(String repo)
	{
		this.repo = repo;
	}
}
