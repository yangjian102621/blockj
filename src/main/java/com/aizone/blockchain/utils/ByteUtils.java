package com.aizone.blockchain.utils;

/**
 * 数组工具
 * @author yangjian
 * @since 18-4-9
 */
public class ByteUtils {

	/**
	 * 两个byte[]数组相加
	 * @param data1
	 * @param data2
	 * @return
	 */
	public static byte[] add(byte[] data1, byte[] data2) {

		byte[] result = new byte[data1.length + data2.length];
		System.arraycopy(data1, 0, result, 0, data1.length);
		System.arraycopy(data2, 0, result, data1.length, data2.length);

		return result;
	}
}
