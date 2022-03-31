package org.rockyang.jblock.db;

import java.util.List;
import java.util.Optional;

/**
 * 数据库操作接口
 * @author yangjian
 * @since 18-4-10
 */
public interface Datastore {

	// save an item into database
	boolean put(String key, Object value);
	// get an item from database with the specified key
	Optional<Object> get(String key);
	// delete an item from database with the specified key
	boolean delete(String key);
	// search in database with key prefix
	List<Object> search(String keyPrefix);
	// close the database
	void close();
}
