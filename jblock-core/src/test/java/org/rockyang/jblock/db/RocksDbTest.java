package org.rockyang.jblock.db;

import org.junit.Test;
import org.rockyang.jblock.conf.MinerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

/**
 * @author yangjian
 * @since 18-4-10
 */
public class RocksDbTest {

	static Logger logger = LoggerFactory.getLogger(RocksDbTest.class);

	static final String KEY = "test-data";
	private static final Datastore datastore = new RocksDatastore(new MinerConfig());

	@Test
	public void put()
	{
		//put data
		String data = UUID.randomUUID().toString();
		datastore.put(KEY, data);
		//get data by key
		Optional<Object> o = datastore.get(KEY);
		if (o.isPresent()) {
			String s = (String) o.get();
			logger.info(s);
			assert data.equals(s);
		}
	}

}