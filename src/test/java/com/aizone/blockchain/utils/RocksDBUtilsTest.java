package com.aizone.blockchain.utils;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * @author yangjian
 * @since 18-4-10
 */
public class RocksDBUtilsTest {

	static Logger logger = LoggerFactory.getLogger(RocksDBUtilsTest.class);

	static final String KEY = "test-data";

	@Test
	public void main() throws Exception {
		//put data
		String data = UUID.randomUUID().toString();
		RocksDBUtils.getInstance().put(KEY, data);
		//get data by key
		String o = (String) RocksDBUtils.getInstance().get(KEY);
		logger.info(o);
		assert data.equals(o);
	}
}
