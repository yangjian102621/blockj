package org.rockyang.jblock.client.test;

import org.junit.Assert;
import org.junit.Test;
import org.rockyang.jblock.client.vo.res.MessageStatusRes;

import java.math.BigDecimal;

/**
 * @author yangjian
 */
public class MessageTest extends BaseTester {

	@Test
	public void sendTransaction()
	{
		String from = "t1uoarr4r2kw2g24c7lrnsxcsv3xfiknlbcp6zpda";
		String to = "t1esjjrygs7adcfbjnodbpdjzulzobznnln4tmsxq";
		BigDecimal value = BigDecimal.valueOf(123.456);
		BigDecimal gasPrice = BigDecimal.valueOf(0.001);
		Integer gasLimit = 300;
		String cid = filecoin.sendTransaction(from, to, value, gasPrice, gasLimit);
		Assert.assertNotNull(cid);
		logger.info("CID: " + cid);
	}

	/**
	 * 查询交易状态
	 */
	@Test
	public void getTransactionStatus()
	{
		String cid = "zDPWYqFD4mNgMtQ8GebcxHnie8YhAHYK3GVAAYfqe2VtmbstLZog";
		MessageStatusRes.Message message = filecoin.getTransaction(cid);
		logger.info("message： " + message);
		if (message.isSuccess()) {
			logger.info("Success.");
		} else {
			logger.warn("Message is unconfirmed");
		}
	}
}
