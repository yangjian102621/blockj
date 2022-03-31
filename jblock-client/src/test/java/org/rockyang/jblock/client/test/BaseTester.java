package org.rockyang.jblock.client.test;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.rockyang.jblock.client.rpc.JBlockServiceWrapper;

/**
 * @author yangjian
 */
public abstract class BaseTester {

	protected static Logger logger = Logger.getLogger(BaseTester.class);
	protected JBlockServiceWrapper JBlockServiceWrapper;

	@Before
	public void init()
	{
		JBlockServiceWrapper = new JBlockServiceWrapper("http://127.0.0.1:3453", true);
	}
}
