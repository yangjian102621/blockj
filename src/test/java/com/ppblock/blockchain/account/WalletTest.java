package com.ppblock.blockchain.account;

import com.ppblock.blockchain.crypto.BtcAddress;
import com.ppblock.blockchain.crypto.ECKeyPair;
import com.ppblock.blockchain.crypto.Keys;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 钱包服务单元测试
 * @author yangjian
 * @since 18-4-16
 */
public class WalletTest {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void generateWallet() throws Exception {

		ECKeyPair ecKeyPair = Keys.createEcKeyPair();
		String address = BtcAddress.getAddress(ecKeyPair.getPublicKey().getEncoded());

		logger.info("address: "+ address);
	}

}
