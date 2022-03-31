package org.rockyang.jblock.client.vo.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * send message response VO
 * @author yangjian
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class SendMessageRes {

	@JsonProperty("Cid")
	private Cid cid;

	@JsonProperty("GasUsed")
	private Integer gasUsed;

	@JsonProperty("Preview")
	private Boolean preview;

	public Cid getCid() {
		return cid;
	}

	public void setCid(Cid cid) {
		this.cid = cid;
	}

	public Integer getGasUsed() {
		return gasUsed;
	}

	public void setGasUsed(Integer gasUsed) {
		this.gasUsed = gasUsed;
	}

	public Boolean getPreview() {
		return preview;
	}

	public void setPreview(Boolean preview) {
		this.preview = preview;
	}
}
