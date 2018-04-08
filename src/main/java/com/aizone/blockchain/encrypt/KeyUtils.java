package com.aizone.blockchain.encrypt;

import com.aizone.blockchain.constants.EncryptConstants;

import java.security.*;
import java.security.spec.ECGenParameterSpec;

/**
 * 密钥工具类
 * @author yangjian
 * @since 18-4-8
 */
public class KeyUtils {


	/**
	 * 生成一个密钥对
	 * @return
	 */
	public static KeyPair generateKeyPair() {

		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance(EncryptConstants.KEY_GEN_ALGORITHM,
					EncryptConstants.KEY_GEN_PROVIDER);
			SecureRandom random = SecureRandom.getInstance(EncryptConstants.SECURE_RANDOM_ALGORITHM);
			ECGenParameterSpec ecSpec = new ECGenParameterSpec(EncryptConstants.ECGEN_PARAM_SPEC);
			// Initialize the key generator and generate a KeyPair
			keyGen.initialize(ecSpec, random);
			KeyPair keyPair = keyGen.generateKeyPair();
			return keyPair;

		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String genAddress(PublicKey publicKey) {
		return null;
	}
}
