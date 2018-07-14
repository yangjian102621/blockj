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
	static final File WALLET_DIR = new File("./keystore");
	static final String WALLET_PASS = "123456";

	//初始化
	static {
		if (!WALLET_DIR.exists()) {
			WALLET_DIR.mkdir();
		}
	}

	/**
	 * 生成默认普通钱包
	 * @throws Exception
	 */
	@Test
	public void generateWallet() throws Exception {

		String password = "123456";
		String name = WalletUtils.generateNewWalletFile(password, walletDir);
		logger.info("wallet name: "+name);
	}

}
