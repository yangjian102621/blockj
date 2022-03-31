package org.rockyang.jblock.client.test;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author yangjian
 */
public class AddressTest extends BaseTester {


	@Test
	public void  newAddress()
	{
		String address = filecoin.newAddress();
		Assert.assertNotNull(address);
		logger.info("Address: " + address);
	}

	@Test
	public void getAddressList()
	{
		List<String> addresses = filecoin.getAddressList();
		Assert.assertNotNull(addresses);
		Assert.assertTrue(addresses.size() > 0);
		for (String address : addresses) {
			logger.info("Address: " + address);
		}
	}

	@Test
	public void getDefaultAddress()
	{
		String address = filecoin.getDefaultAddress();
		Assert.assertNotNull(address);
		logger.info("Default Address: " + address);
	}

}
