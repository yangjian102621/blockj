package com.aizone.blockchain.utils;

import com.aizone.blockchain.core.Block;
import com.aizone.blockchain.db.DBAccess;
import com.aizone.blockchain.db.RocksDBAccess;
import com.aizone.blockchain.wallet.Account;
import com.google.common.base.Optional;

/**
 * 数据库工具类
 * @author yangjian
 * @since 18-4-13
 */
public class DBUtils {

	static DBAccess db;

	static {
		db = new RocksDBAccess();
	}

	/**
	 * 更新最新一个区块的Hash值
	 * @param lastBlock
	 * @return
	 */
	public static boolean putLastBlockIndex(Object lastBlock) {
		return db.putLastBlockIndex(lastBlock);
	}

	/**
	 * 获取最新一个区块的Hash值
	 * @return
	 */
	public static Optional<Object> getLastBlockIndex() {
		return db.getLastBlockIndex();
	}

	/**
	 * 保存区块
	 * @param block
	 * @return
	 */
	public static boolean putBlock(Block block) {
		return db.putBlock(block);
	}

	/**
	 * 获取指定的区块, 根据区块高度去获取
	 * @param blockIndex
	 * @return
	 */
	public static Optional<Block> getBlock(Object blockIndex) {
		return db.getBlock(blockIndex);
	}

	/**
	 * 获取最后（最大高度）一个区块
	 * @return
	 */
	public static Optional<Block> getLastBlock() {
		return db.getLastBlock();
	}

	/**
	 * 添加一个钱包账户
	 * @param account
	 */
	public static boolean putAccount(Account account) {
		return db.putAccount(account);
	}

	/**
	 * 获取指定的钱包账户
	 * @param address
	 * @return
	 */
	public static Optional<Account> getAccount(String address) {
		return db.getAccount(address);
	}

	/**
	 * 设置挖矿账户
	 * @param account
	 */
	public static boolean putCoinBaseAccount(Optional<Account> account) {
		return db.putCoinBaseAccount(account);
	}

	/**
	 * 获取挖矿账户
	 * @return
	 */
	public static Optional<Account> getCoinBaseAccount() {
		return db.getCoinBaseAccount();
	}

	/**
	 * 往数据库添加|更新一条数据
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean put(Object key, Object value) {
		return db.put(key, value);
	}

	/**
	 * 获取某一条指定的数据
	 * @param key
	 * @return
	 */
	public static Optional<Object> get(Object key) {
		return db.get(key);
	}

	/**
	 * 删除一条数据
	 * @param key
	 * @return
	 */
	public static boolean delete(Object key) {
		return db.delete(key);
	}
}
