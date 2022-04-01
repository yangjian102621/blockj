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
	public void sendMessage()
	{
		String from = "t1uoarr4r2kw2g24c7lrnsxcsv3xfiknlbcp6zpda";
		String to = "t1esjjrygs7adcfbjnodbpdjzulzobznnln4tmsxq";
		BigDecimal value = BigDecimal.valueOf(123.456);
		String cid = JBlockServiceWrapper.sendMessage(from, to, value);
		Assert.assertNotNull(cid);
		logger.info("CID: " + cid);
	}

	/**
	 * 查询交易状态
	 */
	@Test
	public void getMessage()
	{
		String cid = "zDPWYqFD4mNgMtQ8GebcxHnie8YhAHYK3GVAAYfqe2VtmbstLZog";
		MessageStatusRes message = JBlockServiceWrapper.getMessage(cid);
		logger.info("message： " + message);
	}
}
