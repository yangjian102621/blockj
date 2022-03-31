package org.rockyang.jblock.client.vo.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author yangjian
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Cid {

	@JsonProperty("/")
	private String root;

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}
}
