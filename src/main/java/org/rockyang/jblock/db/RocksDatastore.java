package org.rockyang.jblock.db;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rockyang.jblock.conf.MinerConfig;
import org.rockyang.jblock.utils.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * RocksDB datastore wrapper
 * @author yangjian
 */
@Component
public class RocksDatastore implements Datastore {

	private final static Logger logger = LoggerFactory.getLogger(RocksDatastore.class);

	private RocksDB datastore;

	public RocksDatastore(MinerConfig config) {
		// load base dir from
		String dataPath = String.format("%s/datastore", config.getRepo());
		try {
			File directory = new File(dataPath);
			if (!directory.exists()) {
				if (!directory.mkdirs()) {
					throw new FileNotFoundException(dataPath);
				}
			}
			datastore = RocksDB.open(new Options().setCreateIfMissing(true), dataPath);
		} catch (RocksDBException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean put(String key, Object value) {
		try {
			datastore.put(key.getBytes(), SerializeUtils.serialize(value));
			return true;
		} catch (Exception e) {
			logger.error("failed to save object: {}", e.toString());
			return false;
		}
	}

	@Override
	public Optional<Object> get(String key) {
		try {
			return Optional.of(SerializeUtils.unSerialize(datastore.get(key.getBytes())));
		} catch (Exception e) {
			logger.error("failed to get object: {}", e.toString());
			return Optional.empty();
		}
	}

	@Override
	public boolean delete(String key) {
		try {
			datastore.delete(key.getBytes());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public List<Object> search(String keyPrefix)
	{
		return null;
	}

	@Override
	public void close() {
		if (datastore != null) {
			datastore.close();
		}
	}
}
