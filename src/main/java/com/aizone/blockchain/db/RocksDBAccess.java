package com.aizone.blockchain.db;

import com.aizone.blockchain.core.Block;
import com.aizone.blockchain.utils.SerializeUtils;
import com.aizone.blockchain.wallet.Account;
import com.google.common.base.Optional;
import org.rocksdb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * RocksDB 操作封装
 * @author yangjian
 * @since 18-4-10
 */
public class RocksDBAccess {

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
	public static final String COIN_BASE_ADDRESS = "coinbase_address";
	/**
	 * 最后一个区块的区块高度
	 */
	public static final String LAST_BLOCK_INDEX = BLOCKS_BUCKET_PREFIX+"last_block";

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
	public boolean putLastBlockIndex(Object lastBlock) {
		return this.put(LAST_BLOCK_INDEX, lastBlock);
	}

	/**
	 * 获取最新一个区块的Hash值
	 * @return
	 */
	public Optional<Object> getLastBlockIndex() {
		return this.get(LAST_BLOCK_INDEX);
	}

	/**
	 * 保存区块
	 * @param block
	 * @return
	 */
	public boolean putBlock(Block block) {
		return this.put(BLOCKS_BUCKET_PREFIX + block.getHeader().getIndex(), block);
	}

	/**
	 * 获取指定的区块, 根据区块高度去获取
	 * @param blockIndex
	 * @return
	 */
	public Optional<Block> getBlock(String blockIndex) {

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
	public Optional<Block> getLastBlock() {
		Optional<Object> blockIndex = getLastBlockIndex();
		if (blockIndex.isPresent()) {
			return this.getBlock(blockIndex.get().toString());
		}
		return Optional.absent();
	}

	/**
	 * 添加一个钱包账户
	 * @param account
	 */
	public boolean putAccount(Account account) {
		return this.put(WALLETS_BUCKET_PREFIX + account.getAddress(), account);
	}

	/**
	 * 获取指定的钱包账户
	 * @param address
	 * @return
	 */
	public Optional<Account> getAccount(String address) {

		Optional<Object> object = this.get(WALLETS_BUCKET_PREFIX + address);
		if (object.isPresent()) {
			return Optional.of((Account) object.get());
		}
		return Optional.absent();
	}

	/**
	 * 设置挖矿账户
	 * @param address
	 */
	public boolean putCoinBaseAddress(String address) {
		return this.put(COIN_BASE_ADDRESS, address);
	}

	/**
	 * 获取挖矿账户地址
	 * @return
	 */
	public Optional<String> getCoinBaseAddress() {
		Optional<Object> object = this.get(COIN_BASE_ADDRESS);
		if (object.isPresent()) {
			return Optional.of((String) object.get());
		}
		return Optional.absent();
	}

	/**
	 * 获取挖矿账户
	 * @return
	 */
	public Optional<Account> getCoinBaseAccount() {
		Optional<String> address = getCoinBaseAddress();
		if (address.isPresent()) {
			return getAccount(address.get());
		} else {
			return Optional.absent();
		}
	}

	/**
	 * 往数据库添加|更新一条数据
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean put(String key, Object value) {
		try {
			rocksDB.put(key.getBytes(), SerializeUtils.serialize(value));
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
	public Optional<Object> get(String key) {
		try {
			return Optional.of(SerializeUtils.unSerialize(rocksDB.get(key.getBytes())));
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
	public boolean delete(String key) {
		try {
			rocksDB.delete(key.getBytes());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 根据前缀搜索
	 * @param keyPrefix
	 * @return
	 */
	public <T> List<T> seekByKey(String keyPrefix) {

		ArrayList<T> ts = new ArrayList<>();
		RocksIterator iterator = rocksDB.newIterator(new ReadOptions());
		byte[] key = keyPrefix.getBytes();
		for (iterator.seek(key); iterator.isValid(); iterator.next()) {
			ts.add((T) SerializeUtils.unSerialize(iterator.value()));
		}
		return ts;
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
