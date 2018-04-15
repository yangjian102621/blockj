package com.aizone.blockchain.db;

import com.aizone.blockchain.core.Block;
import com.aizone.blockchain.utils.SerializeUtils;
import com.aizone.blockchain.wallet.Account;
import com.google.common.base.Optional;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DBAccess 的 RocksDB 实现
 * @author yangjian
 * @since 18-4-10
 */
public class RocksDBAccess implements DBAccess {

	static Logger logger = LoggerFactory.getLogger(RocksDBAccess.class);

	/**
	 * 区块数据存储路径
	 */
	public static final String DATA_DIR = "block-data";
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
	 * 挖矿账户
	 */
	public static final String COIN_BASE_ACCOUNT = "coinbase_wallet";

	/**
	 * 最后一个区块的 hash 在 DB 中的存储 key
	 */
	public static final String LAST_BLOCK_HASH_KEY = BLOCKS_BUCKET_PREFIX+"last_block";

	public RocksDBAccess() {
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
	 * @param lastBlock
	 * @return
	 */
	@Override
	public boolean putLastBlockIndex(Object lastBlock) {
		return this.put(LAST_BLOCK_HASH_KEY, lastBlock);
	}

	/**
	 * 获取最新一个区块的Hash值
	 * @return
	 */
	@Override
	public Optional<Object> getLastBlockIndex() {
		return this.get(LAST_BLOCK_HASH_KEY);
	}

	/**
	 * 保存区块
	 * @param block
	 * @return
	 */
	@Override
	public boolean putBlock(Block block) {
		return this.put(BLOCKS_BUCKET_PREFIX + block.getHeader().getIndex(), block);
	}

	/**
	 * 获取指定的区块, 根据区块高度去获取
	 * @param blockIndex
	 * @return
	 */
	@Override
	public Optional<Block> getBlock(Object blockIndex) {

		Optional<Object> object = this.get(BLOCKS_BUCKET_PREFIX + blockIndex);
		if (object.isPresent()) {
			return Optional.of((Block) object.get());
		}
		return Optional.absent();
	}

	/**
	 * 获取最后（最大高度）一个区块
	 * @return
	 */
	@Override
	public Optional<Block> getLastBlock() {
		Optional<Object> blockIndex = getLastBlockIndex();
		if (blockIndex.isPresent()) {
			return this.getBlock(blockIndex.get());
		}
		return Optional.absent();
	}

	/**
	 * 添加一个钱包账户
	 * @param account
	 */
	@Override
	public boolean putAccount(Account account) {
		return this.put(WALLETS_BUCKET_PREFIX + account.getAddress(), account);
	}

	/**
	 * 获取指定的钱包账户
	 * @param address
	 * @return
	 */
	@Override
	public Optional<Account> getAccount(String address) {

		Optional<Object> object = this.get(WALLETS_BUCKET_PREFIX + address);
		if (object.isPresent()) {
			return Optional.of((Account) object.get());
		}
		return Optional.absent();
	}

	/**
	 * 设置挖矿账户
	 * @param account
	 */
	@Override
	public boolean putCoinBaseAccount(Optional<Account> account) {
		if (account.isPresent()) {
			return this.put(COIN_BASE_ACCOUNT, account.get());
		}
		return false;
	}

	/**
	 * 获取挖矿账户
	 * @return
	 */
	@Override
	public Optional<Account> getCoinBaseAccount() {
		Optional<Object> object = this.get(COIN_BASE_ACCOUNT);
		if (object.isPresent()) {
			return Optional.of((Account) object.get());
		}
		return Optional.absent();
	}

	/**
	 * 往数据库添加|更新一条数据
	 * @param key
	 * @param value
	 * @return
	 */
	@Override
	public boolean put(Object key, Object value) {
		try {
			rocksDB.put(SerializeUtils.serialize(key), SerializeUtils.serialize(value));
			return true;
		} catch (Exception e) {
			logger.error("ERROR for RocksDB : {}", e);
			return false;
		}
	}

	/**
	 * 获取某一条指定的数据
	 * @param key
	 * @return
	 */
	@Override
	public Optional<Object> get(Object key) {
		try {
			return Optional.of(SerializeUtils.unSerialize(rocksDB.get(SerializeUtils.serialize(key))));
		} catch (Exception e) {
			logger.error("ERROR for RocksDB : {}", e);
			return Optional.absent();
		}
	}

	/**
	 * 删除一条数据
	 * @param key
	 * @return
	 */
	@Override
	public boolean delete(Object key) {
		try {
			rocksDB.delete(SerializeUtils.serialize(key));
			return true;
		} catch (Exception e) {
			return false;
		}
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
