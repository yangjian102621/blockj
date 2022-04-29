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
		Long head = serviceWrapper.chainHead();
		System.out.println(head);
		Assert.assertNotNull(head);
	}
}
