package org.rockyang.jblock.client.test;

import org.junit.Assert;
import org.junit.Test;
import org.rockyang.jblock.client.vo.res.KeyInfo;

import java.math.BigDecimal;

/**
 * @author yangjian
 */
public class WalletTest extends BaseTester {


	@Test
	public void walletExport()
	{
		String address = "t1b3keswmeuk4tipp5egjbk3aoag56g5zd3cle2va";
		KeyInfo keyInfo = JBlockServiceWrapper.walletExport(address);
		logger.info(keyInfo);
	}

	@Test
	public void walletImport()
	{
		String privateKey = "pdHwTOrJXnAGvQ0861k66xRsiT7N3Ms8IGte3nT837E=";
		String address = JBlockServiceWrapper.walletImport(privateKey);
		Assert.assertNotNull(address);
		logger.info("wallet import successfully, Address : " + address);
	}

	@Test
	public void  getBalance()
	{
		String address = "t1esjjrygs7adcfbjnodbpdjzulzobznnln4tmsxq";
		BigDecimal balance = JBlockServiceWrapper.getBalance(address);
		logger.info(balance);
	}


}
