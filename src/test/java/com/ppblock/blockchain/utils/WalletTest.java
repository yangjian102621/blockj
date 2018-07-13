package com.ppblock.blockchain.utils;

import com.ppblock.blockchain.crypto.WalletUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 生成钱包测试
 * @author yangjian
 * @since 18-4-9
 */
public class WalletTest {

	static Logger logger = LoggerFactory.getLogger(WalletTest.class);

	@Test
	public void generateWallet() throws Exception {

		String password = "123456";
		File walletDir = new File("./keystore");
		if (!walletDir.exists()) {
			walletDir.mkdir();
		}
		String name = WalletUtils.generateNewWalletFile(password, walletDir);
		logger.info("wallet name: "+name);
	}

}
