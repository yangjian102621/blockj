package com.aizone.blockchain;

import com.aizone.blockchain.encrypt.HashUtils;
import org.junit.Test;

/**
 * @author yangjian
 * @since 2018-04-07 下午8:35.
 */
public class ShaTest {

	@Test
	public void sha256Test() {
		String key = HashUtils.sha256Hex("aizone");
		System.out.println(key.length());
	}

}