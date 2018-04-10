package com.aizone.blockchain.wallet;

import com.aizone.blockchain.core.Block;
import com.aizone.blockchain.utils.SerializeUtils;
import com.google.common.base.Optional;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

/**
 * Rocks DB 操作工具类
 * @author yangjian
 * @since 18-4-10
 */
public class RocksDBUtils {

	/**
	 * 区块数据存储路径
	 */
	public static final String DATA_DIR = "block-data";
	private volatile static RocksDBUtils instance;
	private RocksDB rocksDB;
	/**
	 * 区块数据存储 hash 桶前缀
	 */
	public static final String BLOCKS_BUCKET_PREFIX = "blocks_";
	/**
	 * 钱包数据存储 hash 桶前缀
	 */
	public static final String WALLETS_BUCKET_PREFIX = "wallets_";

	/**
	 * 最后一个区块的 hash 在 DB 中的存储 key
	 */
	public static final String LAST_BLOCK_HASH_KEY = BLOCKS_BUCKET_PREFIX+"last_block";

	/**
	 * 获取 RocksDBUtils 单例
	 * @return
	 */
	public static RocksDBUtils getInstance() {
		if (null == instance) {
			synchronized (RocksDBUtils.class) {
				if (instance == null) {
					instance = new RocksDBUtils();
				}
			}
		}
		return instance;
	}

	private RocksDBUtils() {
		initRocksDB();
	}

	/**
	 * 初始化RocksDB
	 */
	private void initRocksDB() {
		try {
			rocksDB = RocksDB.open(new Options().setCreateIfMissing(true), DATA_DIR);
		} catch (RocksDBException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新最新一个区块的Hash值
	 * @param lastBlockHash
	 */
	public void updateLastBlockHash(String lastBlockHash) throws RocksDBException {
		rocksDB.put(SerializeUtils.serialize(LAST_BLOCK_HASH_KEY), SerializeUtils.serialize(lastBlockHash));
	}

	/**
	 * 获取最新一个区块的Hash值
	 * @return
	 */
	public Optional<String> getLastBlockHash() throws RocksDBException {
		byte[] lastBlockHashBytes = rocksDB.get(SerializeUtils.serialize(LAST_BLOCK_HASH_KEY));
		if (lastBlockHashBytes != null) {
			return Optional.of((String) SerializeUtils.unSerialize(lastBlockHashBytes));
		}
		return Optional.absent();
	}

	/**
	 * 保存区块
	 *
	 * @param block
	 */
	public void putBlock(Block block) throws RocksDBException {
		byte[] key = SerializeUtils.serialize(BLOCKS_BUCKET_PREFIX + block.getHash());
		rocksDB.put(key, SerializeUtils.serialize(block));
	}

	/**
	 * 获取指定的区块
	 * @param blockHash
	 * @return
	 */
	public Block getBlock(String blockHash) throws RocksDBException {
		byte[] key = SerializeUtils.serialize(BLOCKS_BUCKET_PREFIX + blockHash);
		return (Block) SerializeUtils.unSerialize(rocksDB.get(key));
	}

	/**
	 * 添加一个钱包账户
	 * @param account
	 * @throws RocksDBException
	 */
	public void putAccount(Account account) throws RocksDBException {
		byte[] key = SerializeUtils.serialize(WALLETS_BUCKET_PREFIX + account.getAddress());
		rocksDB.put(key, SerializeUtils.serialize(account));
	}

	/**
	 * 获取指定的钱包账户
	 * @param address
	 * @return
	 */
	public Account getAccount(String address) throws RocksDBException {
		byte[] key = SerializeUtils.serialize(WALLETS_BUCKET_PREFIX + address);
		return (Account) SerializeUtils.unSerialize(rocksDB.get(key));
	}

	/**
	 * 往数据库添加|更新一条数据
	 * @param key
	 * @param value
	 * @throws RocksDBException
	 */
	public void put(String key, Object value) throws RocksDBException {
		rocksDB.put(SerializeUtils.serialize(key), SerializeUtils.serialize(value));
	}

	/**
	 * 获取某一条指定的数据
	 * @param key
	 * @return
	 * @throws RocksDBException
	 */
	public Object get(String key) throws RocksDBException {
		return SerializeUtils.unSerialize(rocksDB.get(SerializeUtils.serialize(key)));
	}

	/**
	 * 关闭数据库
	 */
	public void closeDB() {
		if (null != rocksDB) {
			rocksDB.close();
		}
	}
}
