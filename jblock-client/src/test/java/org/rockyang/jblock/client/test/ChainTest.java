package org.rockyang.jblock.client.test;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author yangjian
 */
public class ChainTest extends BaseTester {

	@Test
	public void chainHead()
	{
		String cid = JBlockServiceWrapper.chainHead();
		System.out.println(cid);
		Assert.assertNotNull(cid);
	}
}
