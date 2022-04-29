package org.rockyang.jblock.client.test;

import org.junit.Test;

/**
 * @author yangjian
 */
public class WalletTest extends BaseTester {


	@Test
	public void newWallet()
	{
		String address = serviceWrapper.newWallet();
		logger.info(address);
	}

}
