package org.rockyang.blockj.client.test;

import org.junit.Before;
import org.rockyang.blockj.client.rpc.impl.BlockServiceImpl;

/**
 * @author yangjian
 */
public abstract class BaseTester {

	protected BlockServiceImpl serviceWrapper;

	@Before
	public void init()
	{
		serviceWrapper = new BlockServiceImpl("http://127.0.0.1:8001", true);
	}
}
