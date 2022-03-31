package org.rockyang.jblock.client.vo.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * wallet key information
 * @author yangjian
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class KeyInfo {

	private String privateKey;
	private String address;
	private String curve;

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCurve() {
		return curve;
	}

	public void setCurve(String curve) {
		this.curve = curve;
	}

	@Override
	public String toString() {
		return "KeyInfo{" +
				"privateKey='" + privateKey + '\'' +
				", address='" + address + '\'' +
				", curve='" + curve + '\'' +
				'}';
	}
}
