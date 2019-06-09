package org.rockyang.blockchain.web.vo.req;

/**
 * vo params for sending transaction
 * @author yangjian
 * @since 19-6-9 下午1:57
 */
import java.math.BigDecimal;

/**
 * 发送交易参数 VO
 * @author yangjian
 * @since 18-4-13
 */
public class TransactionVo {

	/**
	 * 收款人地址
	 */
	private String to;

	/**
	 * 交易金额
	 */
	private BigDecimal amount;

	/**
	 * 付款人私钥
	 */
	private String priKey;

	/**
	 * 附加数据
	 */
	private String data;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getPriKey() {
		return priKey;
	}

	public void setPriKey(String priKey) {
		this.priKey = priKey;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
