package com.aizone.blockchain;

import com.aizone.blockchain.utils.SerializeUtils;
import com.aizone.blockchain.wallet.Account;
import com.aizone.blockchain.wallet.Personal;
import org.junit.Test;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

/**
 * @author yangjian
 * @since 18-4-10
 */
public class RocksDBTest {

	static final String DATA_DIR = "block-data";
	static final String KEY = "test-data";
	static final String WALLET_FILE = "My-Wallet";

	@Test
	public void write() throws Exception {
		RocksDB rocksDB = RocksDB.open(new Options().setCreateIfMissing(true), DATA_DIR);
		Account account = Personal.newAccount();
		System.out.println(account.getAddress());
		rocksDB.put(SerializeUtils.serialize(KEY), SerializeUtils.serialize(account));
	}

	@Test
	public void read() throws RocksDBException {
		RocksDB rocksDB = RocksDB.open(new Options().setCreateIfMissing(true), DATA_DIR);
		byte[] bytes = rocksDB.get(SerializeUtils.serialize(KEY));
		if (null != bytes) {
			Account account = (Account) SerializeUtils.unSerialize(bytes);
			//1MR78nMoUrhqrWKAwmQEqh2YMzNym4zhGm
			System.out.println(account.getAddress());
		}
	}
}
