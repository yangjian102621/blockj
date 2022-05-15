package org.rockyang.blockj.client.test;

import org.junit.Before;
import org.rockyang.blockj.client.rpc.impl.BlockjServiceImpl;

/**
 * @author yangjian
 */
public abstract class BaseTester {

	protected BlockjServiceImpl serviceWrapper;

	@Before
	public void init()
	{
		serviceWrapper = new BlockjServiceImpl("http://127.0.0.1:8001", true);
	}
}
