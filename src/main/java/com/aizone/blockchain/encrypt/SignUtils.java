package com.aizone.blockchain.encrypt;

import com.aizone.blockchain.constants.EncryptConstants;
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;

import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.ECPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 签名工具类
 * @author yangjian
 * @since 18-4-10
 */
public class SignUtils {

	/**
	 * 通过字符串私钥生成 PrivateKey 对象
	 * @param sCode PrivateKey 的专用密钥
	 * @return
	 */
	public static PrivateKey generatePrivateKey(String sCode) throws Exception {

		ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec(EncryptConstants.EC_PARAM_SPEC);
		ECPrivateKeySpec keySpec = new ECPrivateKeySpec(new BigInteger(sCode, 16), ecSpec);
		Security.addProvider(new BouncyCastleProvider());
		KeyFactory keyFactory = KeyFactory.getInstance(EncryptConstants.KEY_GEN_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
		return keyFactory.generatePrivate(keySpec);
	}

	/**
	 * 通过 byte[] 公钥生成 PublicKey 对象
	 * @param publicKey
	 * @return
	 */
	public static PublicKey generatePublicKey(byte[] publicKey) throws Exception {

		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
		Security.addProvider(new BouncyCastleProvider());
		KeyFactory keyFactory = KeyFactory.getInstance(EncryptConstants.KEY_GEN_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
		ECPublicKey pubKey = (ECPublicKey) keyFactory.generatePublic(x509KeySpec);
		return pubKey;

//		Security.addProvider(new BouncyCastleProvider());
//		ECParameterSpec ecParameters = ECNamedCurveTable.getParameterSpec(EncryptConstants.EC_PARAM_SPEC);
//		// 使用椭圆曲线 x,y 点去生成公钥Key
//		BigInteger x = new BigInteger(1, Arrays.copyOfRange(publicKey, 1, 33));
//		BigInteger y = new BigInteger(1, Arrays.copyOfRange(publicKey, 33, 65));
//		ECPoint ecPoint = ecParameters.getCurve().createPoint(x, y);
//
//		ECPublicKeySpec keySpec = new ECPublicKeySpec(ecPoint, ecParameters);
//		KeyFactory keyFactory = KeyFactory.getInstance(EncryptConstants.KEY_GEN_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
//		return keyFactory.generatePublic(keySpec);
	}

	/**
	 * 使用私钥对源数据签名
	 * @param privateKey
	 * @param data
	 * @return
	 */
	public static String sign(String privateKey, String data) throws Exception {

		return sign(generatePrivateKey(privateKey), data);
	}

	public static String sign(PrivateKey privateKey, String data) throws Exception {

		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
		Security.addProvider(new BouncyCastleProvider());
		KeyFactory keyFactory = KeyFactory.getInstance(EncryptConstants.KEY_GEN_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
		PrivateKey pkcs8PrivateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		Signature signature = Signature.getInstance(EncryptConstants.SIGN_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
		signature.initSign(pkcs8PrivateKey);
		signature.update(data.getBytes());
		byte[] res = signature.sign();
		return HexBin.encode(res);
	}

	/**
	 * 使用公钥验证签名
	 * @param publicKey
	 * @param sign
	 * @param data
	 * @return
	 */
	public static boolean verify(byte[] publicKey, String sign, String data) throws Exception {

		return verify(generatePublicKey(publicKey), sign, data);
	}

	public static boolean verify(PublicKey publicKey, String sign, String data) throws Exception {

		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
		Security.addProvider(new BouncyCastleProvider());
		KeyFactory keyFactory = KeyFactory.getInstance(EncryptConstants.KEY_GEN_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
		PublicKey x509PublicKey = keyFactory.generatePublic(x509EncodedKeySpec);
		Signature signature = Signature.getInstance(EncryptConstants.SIGN_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
		signature.initVerify(x509PublicKey);
		signature.update(data.getBytes());
		return signature.verify(HexBin.decode(sign));
	}

}
