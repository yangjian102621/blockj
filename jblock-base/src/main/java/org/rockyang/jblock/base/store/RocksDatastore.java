package org.rockyang.jblock.base.store;

import org.rocksdb.*;
import org.rockyang.jblock.base.utils.SerializeUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * RocksDB datastore wrapper
 *
 * @author yangjian
 */
public class RocksDatastore implements Datastore {

	private RocksDB datastore;

	public RocksDatastore(String repo)
	{
		// load base dir from
		String dataPath = String.format("%s/datastore", repo);
		try {
			File directory = new File(dataPath);
			if (!directory.exists() && !directory.mkdirs()) {
				throw new FileNotFoundException(dataPath);
			}
			datastore = RocksDB.open(new Options().setCreateIfMissing(true), dataPath);
		} catch (RocksDBException | FileNotFoundException e) {
			//e.printStackTrace();
		}
	}

	@Override
	public boolean put(String key, Object value)
	{
		try {
			datastore.put(key.getBytes(), SerializeUtils.serialize(value));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Optional<Object> get(String key)
	{
		try {
			return Optional.of(SerializeUtils.unSerialize(datastore.get(key.getBytes())));
		} catch (Exception e) {
			//e.printStackTrace();
			return Optional.empty();
		}
	}

	@Override
	public boolean delete(String key)
	{
		try {
			datastore.delete(key.getBytes());
			return true;
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> search(String keyPrefix)
	{
		ArrayList<T> list = new ArrayList<>();
		RocksIterator iterator = datastore.newIterator(new ReadOptions());
		byte[] key = keyPrefix.getBytes();
		for (iterator.seek(key); iterator.isValid(); iterator.next()) {
			list.add((T) SerializeUtils.unSerialize(iterator.value()));
		}
		return list;
	}

	@Override
	public void close()
	{
		if (datastore != null) {
			datastore.close();
		}
	}
}
