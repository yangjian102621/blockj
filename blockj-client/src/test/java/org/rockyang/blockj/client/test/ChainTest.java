package org.rockyang.blockj.client.test;

import org.junit.Test;

/**
 * @author yangjian
 */
public class ChainTest extends BaseTester {

	@Test
	public void chainHead()
	{
		Long head = serviceWrapper.chainHead();
		System.out.printf("Chain head: %d\n", head);
	}
}
