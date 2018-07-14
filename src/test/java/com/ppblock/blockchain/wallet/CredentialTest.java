package com.ppblock.blockchain.wallet;

import com.ppblock.blockchain.crypto.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ppblock.blockchain.wallet.WalletTest.WALLET_DIR;
import static com.ppblock.blockchain.wallet.WalletTest.WALLET_PASS;


/**
 * 凭证测试
 * @author yangjian
 * @since 18-7-14
 */
public class CredentialTest {

	static Logger logger = LoggerFactory.getLogger(CredentialTest.class);

	/**
	 * 通过私钥字符串创建凭证
	 * @throws Exception
	 */
	@Test
	public void createByPrivateKey() throws Exception {

		String privateKey = "bc3da6fa7ab05c21a1087e93206ce7635bc4be0a23340211174662441862217e";
		Credentials credentials = Credentials.create(privateKey);
		logger.info("ether address: "+ credentials.getAddress());
		logger.info("btc address: "+ credentials.getBtcAddress());
		logger.info("privateKey: "+ credentials.getEcKeyPair().exportPrivateKey());
	}

	/**
	 * 通过 KeyPair 创建凭证
	 * @throws Exception
	 */
	@Test
	public void createByKeypair() throws Exception {

		ECKeyPair keyPair = Keys.createEcKeyPair();
		Credentials credentials = Credentials.create(keyPair);
		logger.info("ether address: "+ credentials.getAddress());
		logger.info("btc address: "+ credentials.getBtcAddress());
		logger.info("privateKey: "+ credentials.getEcKeyPair().exportPrivateKey());
	}

	/**
	 * 使用 keystore + password 加载凭证
	 */
	@Test
	public void loadCredentialsFromWallet() throws Exception {

		String walletFile = WALLET_DIR+"/UTC--2018-07-14T06-22-58.622000000Z" +
				"--0x74704f8be564c681e042e37f33efb12fc631b87c.json";
		Credentials credentials = WalletUtils.loadCredentials(WALLET_PASS, walletFile);
		logger.info("ether address: "+ credentials.getAddress());
		logger.info("btc address: "+ credentials.getBtcAddress());
		logger.info("privateKey: "+ credentials.getEcKeyPair().exportPrivateKey());
	}

	/**
	 * 使用助记词导入凭证(从助记词恢复钱包)
	 */
	@Test
	public void loadCredentialsFromMemorizingWords() throws Exception {
		//educate bread attract theme obey squirrel busy food finish segment sell audit
		//0xce7d01da2b1cfe5b65f35924127fa8f746a00050
		String memorizingWords = "educate bread attract theme obey squirrel busy food finish segment sell audit";
		Credentials credentials = WalletUtils.loadBip39Credentials(memorizingWords);
		logger.info("ether address: "+ credentials.getAddress());
		logger.info("btc address: "+ credentials.getBtcAddress());
		logger.info("privateKey: "+ credentials.getEcKeyPair().exportPrivateKey());
	}

	/**
	 * 使用 （助记词+密码） 导入凭证
	 * test datas:
	 * memorizing word: worth flush raise credit unable very easily edge near nuclear video vicious
	 * address: 0x7154dbe7a2f9f1a9632f886201efdf996627b387
	 * password: 123456
	 *
	 * 这里需要注意的是：
	 * 如果助记词或者密码不对，系统不会报错的，只是导入的地址就跟原来的不一样，因为从理论上来说，任意的助记词和密码的组合都是可以生成一个
	 * 唯一的钱包地址的
	 */
	@Test
	public void loadCredentialsWithWordsAndPass() throws Exception {

		String words = "worth flush raise credit unable very easily edge near nuclear video vicious";
		String password = "123456";

		Credentials credentials = WalletUtils.loadBip39Credentials(password, words);
		logger.info("ether address: "+ credentials.getAddress());
		logger.info("btc address: "+ credentials.getBtcAddress());
		logger.info("privateKey: "+ credentials.getEcKeyPair().exportPrivateKey());
	}
}
