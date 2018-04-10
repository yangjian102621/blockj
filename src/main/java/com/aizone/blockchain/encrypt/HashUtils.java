package com.aizone.blockchain.encrypt;

import cn.hutool.crypto.digest.DigestUtil;

/**
 * hash 工具类
 * @author yangjian
 * @since 2018-04-07 下午8:32.
 */
public class HashUtils {

	/**
	 * 使用 sha256 算法加密
	 * @param input
	 * @return
	 */
	public static String sha256Hex(String input) {
		return DigestUtil.sha256Hex(input);
	}

	/**
	 * 使用 sha256 hash 算法加密，返回一个 64 位的字符串 hash
	 * @param input
	 * @return
	 */
	public static String sha256Hex(byte[] input) {
		return DigestUtil.sha256Hex(input);
	}

	public static byte[] sha256(String input) {
		return DigestUtil.sha256(input);
	}

	public static byte[] sha256(byte[] input) {
		return DigestUtil.sha256(input);
	}

	/**
	 * 数组转换成十六进制字符串
	 *
	 * @param bArray
	 * @return HexString
	 */
	public static String toHexString(byte[] bArray) {

		StringBuilder sb = new StringBuilder(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2) {
				sb.append(0);
			}
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}
}
