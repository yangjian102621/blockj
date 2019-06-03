package org.rockyang.blockchain.crypto;

import org.rockyang.blockchain.constants.CryptoConstants;
import org.rockyang.blockchain.utils.Numeric;
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;

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
public class Sign {

	private static final X9ECParameters CURVE_PARAMS = CustomNamedCurves.getByName(CryptoConstants.EC_PARAM_SPEC);
	static final ECDomainParameters CURVE = new ECDomainParameters(
			CURVE_PARAMS.getCurve(), CURVE_PARAMS.getG(), CURVE_PARAMS.getN(), CURVE_PARAMS.getH());

	/**
	 * 使用私钥对源数据签名
	 * @param privateKey
	 * @param data
	 * @return
	 */
	public static String sign(String privateKey, String data) throws Exception {

		return sign(privateKeyFromString(privateKey), data);
	}

	public static String sign(BigInteger privateKeyValue, String data) throws Exception {

		return sign(privateKeyFromBigInteger(privateKeyValue), data);
	}

	public static String sign(PrivateKey privateKey, String data) throws Exception {

		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
		Security.addProvider(new BouncyCastleProvider());
		KeyFactory keyFactory = KeyFactory.getInstance(CryptoConstants.KEY_GEN_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
		PrivateKey pkcs8PrivateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		Signature signature = Signature.getInstance(CryptoConstants.SIGN_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
		signature.initSign(pkcs8PrivateKey);
		signature.update(data.getBytes());
		byte[] res = signature.sign();
		return HexBin.encode(res);
	}

	public static String sign(Credentials credentials, String data) throws Exception {

		return sign(credentials.getEcKeyPair().getPrivateKey(), data);
	}

	/**
	 * 使用公钥验证签名
	 * @param publicKey
	 * @param sign
	 * @param data
	 * @return
	 */
	public static boolean verify(byte[] publicKey, String sign, String data) throws Exception {

		return verify(publicKeyFromByte(publicKey), sign, data);
	}

	public static boolean verify(PublicKey publicKey, String sign, String data) throws Exception {

		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
		Security.addProvider(new BouncyCastleProvider());
		KeyFactory keyFactory = KeyFactory.getInstance(CryptoConstants.KEY_GEN_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
		PublicKey x509PublicKey = keyFactory.generatePublic(x509EncodedKeySpec);
		Signature signature = Signature.getInstance(CryptoConstants.SIGN_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
		signature.initVerify(x509PublicKey);
		signature.update(data.getBytes());
		return signature.verify(HexBin.decode(sign));
	}


	/**
	 * 通过秘钥值(BigInteger)生成 PrivateKey 对象
	 * @param privateKeyValue 秘钥值
	 * @return
	 */
	public static PrivateKey privateKeyFromBigInteger(BigInteger privateKeyValue) throws Exception {

		ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec(CryptoConstants.EC_PARAM_SPEC);
		ECPrivateKeySpec keySpec = new ECPrivateKeySpec(privateKeyValue, ecSpec);
		Security.addProvider(new BouncyCastleProvider());
		KeyFactory keyFactory = KeyFactory.getInstance(CryptoConstants.KEY_GEN_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
		return keyFactory.generatePrivate(keySpec);
	}

	/**
	 * 通过秘钥值(16进制字符串)生成 PrivateKey 对象
	 * @param privateKey 秘钥值
	 * @return
	 */
	public static PrivateKey privateKeyFromString(String privateKey) throws Exception {

		return privateKeyFromBigInteger(Numeric.toBigInt(privateKey));
	}

	/**
	 * 通过秘钥值(BigInteger)生成 PrivateKey 对象
	 * @param privateKeyValue 秘钥值
	 * @return
	 */
	public static PublicKey publicKeyFromPrivate(BigInteger privateKeyValue) throws Exception {

		ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec(CryptoConstants.EC_PARAM_SPEC);
		ECPoint point = publicPointFromPrivate(privateKeyValue);
		ECPublicKeySpec keySpec = new ECPublicKeySpec(point, ecSpec);
		Security.addProvider(new BouncyCastleProvider());
		KeyFactory keyFactory = KeyFactory.getInstance(CryptoConstants.KEY_GEN_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
		return keyFactory.generatePublic(keySpec);
	}

	/**
	 * 通过 byte[] 公钥生成 PublicKey 对象
	 * @param publicKey
	 * @return
	 */
	public static PublicKey publicKeyFromByte(byte[] publicKey) throws Exception {

		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
		Security.addProvider(new BouncyCastleProvider());
		KeyFactory keyFactory = KeyFactory.getInstance(CryptoConstants.KEY_GEN_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
		ECPublicKey pubKey = (ECPublicKey) keyFactory.generatePublic(x509KeySpec);
		return pubKey;
	}

	/**
	 * Returns public key point from the given private key.
	 */
	private static ECPoint publicPointFromPrivate(BigInteger privKey) {
		/*
		 * TODO: FixedPointCombMultiplier currently doesn't support scalars longer than the group
		 * order, but that could change in future versions.
		 */
		if (privKey.bitLength() > CURVE.getN().bitLength()) {
			privKey = privKey.mod(CURVE.getN());
		}
		return new FixedPointCombMultiplier().multiply(CURVE.getG(), privKey);
	}

}
