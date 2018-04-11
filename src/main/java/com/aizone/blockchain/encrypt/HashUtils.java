package com.aizone.blockchain.encrypt;

import org.apache.commons.codec.digest.DigestUtils;

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
		return DigestUtils.sha256Hex(input);
	}

	/**
	 * 使用 sha256 hash 算法加密，返回一个 64 位的字符串 hash
	 * @param input
	 * @return
	 */
	public static String sha256Hex(byte[] input) {
		return DigestUtils.sha256Hex(input);
	}

	public static byte[] sha256(String input) {
		return DigestUtils.sha256(input);
	}

	public static byte[] sha256(byte[] input) {
		return DigestUtils.sha256(input);
	}
}
