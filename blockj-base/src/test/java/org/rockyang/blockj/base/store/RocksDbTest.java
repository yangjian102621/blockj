package org.rockyang.blockj.base.store;

import org.junit.Test;

import java.util.Optional;
import java.util.UUID;

/**
 * @author yangjian
 */
public class RocksDbTest {

	static final String KEY = "test-data";
	private static final Datastore datastore = new RocksDatastore("/tmp/rocksdb");

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
			System.out.println(s);
			;
			assert data.equals(s);
		}
	}

}