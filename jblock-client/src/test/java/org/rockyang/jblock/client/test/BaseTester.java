package org.rockyang.jblock.client.test;

import org.junit.Before;
import org.rockyang.jblock.client.rpc.impl.JBlockServiceImpl;

/**
 * @author yangjian
 */
public abstract class BaseTester {

	protected JBlockServiceImpl serviceWrapper;

	@Before
	public void init()
	{
		serviceWrapper = new JBlockServiceImpl("http://127.0.0.1:8001", true);
	}
}
