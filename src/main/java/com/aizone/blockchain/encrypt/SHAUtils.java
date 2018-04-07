package com.aizone.blockchain.encrypt;

import cn.hutool.crypto.digest.DigestUtil;

/**
 * SHA 算法工具类
 * @author yangjian
 * @since 2018-04-07 下午8:32.
 */
public class SHAUtils {

	/**
	 * 使用 sha256 算法加密，返回一个 64 位的字符串
	 * @param input
	 * @return
	 */
	public static String sha256(String input) {
		return DigestUtil.sha256Hex(input);
	}
}
