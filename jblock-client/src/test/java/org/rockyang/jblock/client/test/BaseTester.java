package org.rockyang.jblock.client.test;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.rockyang.jblock.client.rpc.Filecoin;

/**
 * @author yangjian
 */
public abstract class BaseTester {

	protected static Logger logger = Logger.getLogger(BaseTester.class);
	protected Filecoin filecoin;

	@Before
	public void init()
	{
		filecoin = new Filecoin("http://127.0.0.1:3453", true);
	}
}
