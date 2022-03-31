package org.rockyang.jblock.client.test;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author yangjian
 */
public class ConfigTest extends BaseTester {

	@Test
	public void configGet()
	{
		String key = "wallet.defaultAddress";
		String address = (String) filecoin.config(key);
		logger.info("Address : " + address);
	}

	@Test
	public void configSet()
	{
		String key = "heartbeat.nickname";
		String value = "RockYang";
		filecoin.config(key, value);

		Object testAddress = filecoin.config(key);
		Assert.assertNotNull(testAddress);
		logger.info(testAddress);
	}
}
