package org.rockyang.jblock.db;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

/**
 * @author yangjian
 * @since 18-4-10
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = Application.class)
public class RocksDbTest {

	static Logger logger = LoggerFactory.getLogger(RocksDbTest.class);

	static final String KEY = "test-data";

//	@Autowired
//	private Datastore dataStore;

	@Test
	public void put()
	{
		//put data
		Datastore datastore = new RocksDatastore();
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