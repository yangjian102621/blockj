package com.aizone.blockchain.utils;

import com.aizone.blockchain.encrypt.HashUtils;
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * @author yangjian
 * @since 2018-04-07 下午8:35.
 */
public class HashUtilsTest {

	static Logger logger = LoggerFactory.getLogger(HashUtilsTest.class);

	@Test
	public void sha256Hex() {
		String key = HashUtils.sha256Hex("aizone");
		logger.info(key);
	}

	@Test
	public void sha256() {
		ArrayList<String> list = new ArrayList<>();
		list.add("aaaaaaa");
		list.add("bbbbbbb");
		logger.info(HexBin.encode(list.toString().getBytes()));
	}

}
