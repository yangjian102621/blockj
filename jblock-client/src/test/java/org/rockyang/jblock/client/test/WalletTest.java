package org.rockyang.jblock.client.test;

import org.junit.Test;
import org.rockyang.jblock.base.model.Wallet;

import java.util.List;

/**
 * @author yangjian
 */
public class WalletTest extends BaseTester {


	@Test
	public void newWallet()
	{
		String address = serviceWrapper.newWallet();
		System.out.printf("Address: %s\n", address);
	}

	@Test
	public void walletList()
	{
		List<Wallet> accounts = serviceWrapper.walletList();
		for (Wallet wallet : accounts) {
			System.out.printf("Address: %s, Balance: %f\n", wallet.getAddress(), wallet.getBalance());
		}
	}

}
