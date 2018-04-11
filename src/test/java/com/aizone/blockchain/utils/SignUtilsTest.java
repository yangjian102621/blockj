package com.aizone.blockchain.utils;

import com.aizone.blockchain.encrypt.SignUtils;
import com.aizone.blockchain.encrypt.WalletUtils;
import org.junit.Test;

import java.security.KeyPair;

/**
 * 签名测试
 * @author yangjian
 * @since 18-4-9
 */
public class SignUtilsTest {

	@Test
	public void sign() throws Exception {
		KeyPair keyPair = WalletUtils.generateKeyPair();
		String data = "aizone";
		String sign = SignUtils.sign(keyPair.getPrivate(), data);
		System.out.println(WalletUtils.privateKeyToString(keyPair.getPrivate()));
		System.out.println(WalletUtils.publicKeyEncode(keyPair.getPublic().getEncoded()));
		System.out.println(sign);
		System.out.println(SignUtils.verify(keyPair.getPublic(), sign, data));
	}

	@Test
	public void verify() throws Exception {
		byte[] publickey = WalletUtils.publicKeyDecode
				("PZ8Tyr4Nx8MHsRAGMpZmZ6TWY63dXWSCztLEMe26tZJGYxb7eahUwzrgrSGA4nbfhLp2r6NFFL9rFYDrR6UdjEMBZcrVCUWnhvsVgeVXPJr8GVbH25WzL2L7");
		String sign = "304502202101CDD91D0944F7C1E092A1238F8DCA76C70A06F2EBA2C20FB18EC27E7F945E02210096445C078793C1F6CF4DD106C612DF047D8376EF34A59939CADBCBDA97824206";
		String data = "aizone";
		System.out.println(SignUtils.verify(publickey, sign, data));

	}

}
