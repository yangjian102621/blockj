package com.aizone.blockchain.db;

import com.aizone.blockchain.utils.DBUtils;
import com.google.common.base.Optional;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * @author yangjian
 * @since 18-4-10
 */
public class DBUtilsTest {

	static Logger logger = LoggerFactory.getLogger(DBUtilsTest.class);

	static final String KEY = "test-data";

	@Test
	public void main() throws Exception {
		//put data
		String data = UUID.randomUUID().toString();
		DBUtils.put(KEY, data);
		//get data by key
		Optional<Object> o = DBUtils.get(KEY);
		if (o.isPresent()) {
			String s = (String) o.get();
			logger.info(s);
			assert data.equals(s);
		}
	}
}
