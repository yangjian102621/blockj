package org.rockyang.jblock.client.vo.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yangjian
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class KeyInfoReq {

	@JsonProperty("KeyInfo")
	private List<KeyInfo> keyInfos = new ArrayList<KeyInfo>();

	public static final class KeyInfo {

		private String privateKey;
		private String curve;

		public KeyInfo(String privateKey, String curve)
		{
			this.privateKey = privateKey;
			this.curve = curve;
		}

		public String getPrivateKey() {
			return privateKey;
		}

		public void setPrivateKey(String privateKey) {
			this.privateKey = privateKey;
		}

		public String getCurve() {
			return curve;
		}

		public void setCurve(String curve) {
			this.curve = curve;
		}

	}

	public List<KeyInfo> getKeyInfos() {
		return keyInfos;
	}

	public void setKeyInfos(List<KeyInfo> keyInfos) {
		this.keyInfos = keyInfos;
	}

	public void addKeyInfo(KeyInfo keyInfo)
	{
		this.keyInfos.add(keyInfo);
	}
}
