package org.rockyang.jblock.utils;

import org.rockyang.jblock.crypto.BtcAddress;
import org.rockyang.jblock.crypto.ECKeyPair;
import org.rockyang.jblock.crypto.Keys;
import org.rockyang.jblock.crypto.Sign;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PublicKey;

/**
 * 签名测试
 * @author yangjian
 * @since 18-4-9
 */
public class SignTest {

	static Logger logger = LoggerFactory.getLogger(SignTest.class);

	@Test
	public void sign() throws Exception {

		ECKeyPair ecKeyPair = Keys.createEcKeyPair();
		String btcAddress = BtcAddress.getAddress(ecKeyPair.getPublicKey().getEncoded());
		String ethAddress = ecKeyPair.getAddress();
		String data = "ppblock";
		String sign = Sign.sign(ecKeyPair.getPrivateKey(), data);
		logger.info("btc address: "+ btcAddress);
		logger.info("ether address: "+ ethAddress);
		logger.info("private key: "+ ecKeyPair.exportPrivateKey());
		logger.info("public key: "+ ecKeyPair.getPublicKey());
		logger.info("sign: "+ sign);
		logger.info("sign verify result: "+Sign.verify(ecKeyPair.getPublicKey(), sign, data));

		//公钥编码和解码
		String publicKeyEncode = Keys.publicKeyEncode(ecKeyPair.getPublicKey().getEncoded());
		logger.info("sign verify result: "+Sign.verify(Keys.publicKeyDecode(publicKeyEncode), sign, data));

		//从私钥恢复地址
		PublicKey publicKey = Sign.publicKeyFromPrivate(ecKeyPair.getPrivateKeyValue());
		logger.info("address: "+ BtcAddress.getAddress(publicKey.getEncoded()));
	}

}
