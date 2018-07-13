package com.ppblock.blockchain.utils;

import com.ppblock.blockchain.crypto.AddressUtils;
import com.ppblock.blockchain.crypto.ECKeyPair;
import com.ppblock.blockchain.crypto.Keys;
import com.ppblock.blockchain.crypto.SignUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 签名测试
 * @author yangjian
 * @since 18-4-9
 */
public class SignUtilsTest {

	static Logger logger = LoggerFactory.getLogger(SignUtilsTest.class);

	@Test
	public void sign() throws Exception {

		ECKeyPair ecKeyPair = Keys.createEcKeyPair();
		String address = AddressUtils.generateAddress(ecKeyPair.getPublicKey().toByteArray());
		String data = "aizone";
		String sign = SignUtils.sign(ecKeyPair.getPrivateKey().toByteArray(), data);
		logger.info("address: "+ address);
		logger.info("private key: "+ ecKeyPair.getPrivateKey().toString(16));
		logger.info("public key: "+ ecKeyPair.getPublicKey());
		logger.info("sign: "+ sign);
		System.out.println(SignUtils.verify(ecKeyPair.getPublicKey().toByteArray(), sign, data));
	}

	@Test
	public void verify() throws Exception {
		byte[] publickey = AddressUtils.publicKeyDecode
				("PZ8Tyr4Nx8MHsRAGMpZmZ6TWY63dXWSCztLEMe26tZJGYxb7eahUwzrgrSGA4nbfhLp2r6NFFL9rFYDrR6UdjEMBZcrVCUWnhvsVgeVXPJr8GVbH25WzL2L7");
		String sign = "304502202101CDD91D0944F7C1E092A1238F8DCA76C70A06F2EBA2C20FB18EC27E7F945E02210096445C078793C1F6CF4DD106C612DF047D8376EF34A59939CADBCBDA97824206";
		String data = "aizone";
		System.out.println(SignUtils.verify(publickey, sign, data));

	}

}
