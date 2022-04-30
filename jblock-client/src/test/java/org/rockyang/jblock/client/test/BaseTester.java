package org.rockyang.jblock.client.test;

import org.junit.Before;
import org.rockyang.jblock.client.rpc.JBlockServiceWrapper;

/**
 * @author yangjian
 */
public abstract class BaseTester {

	protected JBlockServiceWrapper serviceWrapper;

	@Before
	public void init()
	{
		serviceWrapper = new JBlockServiceWrapper("http://127.0.0.1:8001", true);
	}
}
