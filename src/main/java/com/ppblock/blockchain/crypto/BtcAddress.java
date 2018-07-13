package com.ppblock.blockchain.crypto;

import com.ppblock.blockchain.utils.ByteUtils;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * 钱包工具类
 * @author yangjian
 * @since 18-4-8
 */
public class BtcAddress {

	/**
	 * 加密字符集合
	 */
	private static final String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";

	/**
	 * 根据公钥生成比特币钱包地址
	 * @param publicKey
	 * @return
	 */
	public static String getAddress(byte[] publicKey) {

		//1. 计算公钥的 SHA-256 哈希值
		byte[] sha256Bytes = Hash.sha3(publicKey);
		//2. 取上一步结果，计算 RIPEMD-160 哈希值
		RIPEMD160Digest digest = new RIPEMD160Digest();
		digest.update(sha256Bytes, 0, sha256Bytes.length);
		byte[] ripemd160Bytes = new byte[digest.getDigestSize()];
		digest.doFinal(ripemd160Bytes, 0);
		//3. 取上一步结果，前面加入地址版本号（主网版本号“0x00”）
		byte[] networkID = new BigInteger("00", 16).toByteArray();
		byte[] extendedRipemd160Bytes = ByteUtils.add(networkID, ripemd160Bytes);
		//4. 取上一步结果，计算 SHA-256 哈希值
		byte[] oneceSha256Bytes = Hash.sha3(extendedRipemd160Bytes);
		//5. 取上一步结果，再计算一下 SHA-256 哈希值
		byte[] twiceSha256Bytes = Hash.sha3(oneceSha256Bytes);
		//6. 取上一步结果的前4个字节（8位十六进制）
		byte[] checksum = new byte[4];
		System.arraycopy(twiceSha256Bytes, 0, checksum, 0, 4);
		//7. 把这4个字节加在第5步的结果后面，作为校验
		byte[] binaryAddressBytes = ByteUtils.add(extendedRipemd160Bytes, checksum);
		//8. 把结果用 Base58 编码算法进行一次编码
		return Base58.encode(binaryAddressBytes);
	}

	/**
	 * 验证地址是否合法
	 * @param address
	 * @return
	 */
	public static boolean verifyAddress(String address) {

		if (address.length() < 26 || address.length() > 35) {
			return false;
		}
		byte[] decoded = decodeBase58To25Bytes(address);
		if (null == decoded) {
			return false;
		}
		// 验证校验码
		byte[] hash1 = Hash.sha3(Arrays.copyOfRange(decoded, 0, 21));
		byte[] hash2 = Hash.sha3(hash1);

		return Arrays.equals(Arrays.copyOfRange(hash2, 0, 4), Arrays.copyOfRange(decoded, 21, 25));
	}

	/**
	 * 使用 Base58 把地址解码成 25 字节
	 * @param input
	 * @return
	 */
	private static byte[] decodeBase58To25Bytes(String input) {

		BigInteger num = BigInteger.ZERO;
		for (char t : input.toCharArray()) {
			int p = ALPHABET.indexOf(t);
			if (p == -1) {
				return null;
			}
			num = num.multiply(BigInteger.valueOf(58)).add(BigInteger.valueOf(p));
		}

		byte[] result = new byte[25];
		byte[] numBytes = num.toByteArray();
		System.arraycopy(numBytes, 0, result, result.length - numBytes.length, numBytes.length);
		return result;
	}
}
