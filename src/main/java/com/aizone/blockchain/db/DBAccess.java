package com.aizone.blockchain.db;

import com.aizone.blockchain.core.Block;
import com.aizone.blockchain.wallet.Account;
import com.google.common.base.Optional;

/**
 * @author yangjian
 * @since 18-4-13
 */
public interface DBAccess {

	/**
	 * 更新最新一个区块的Hash值
	 * @param lastBlock
	 * @return
	 */
	public boolean putLastBlockIndex(Object lastBlock);

	/**
	 * 获取最新一个区块的Hash值
	 * @return
	 */
	public Optional<Object> getLastBlockIndex();

	/**
	 * 保存区块
	 * @param block
	 * @return
	 */
	public boolean putBlock(Block block);

	/**
	 * 获取指定的区块, 根据区块高度去获取
	 * @param blockIndex
	 * @return
	 */
	public Optional<Block> getBlock(Object blockIndex);

	/**
	 * 获取最后（最大高度）一个区块
	 * @return
	 */
	public Optional<Block> getLastBlock();

	/**
	 * 添加一个钱包账户
	 * @param account
	 * @return
	 */
	public boolean putAccount(Account account);

	/**
	 * 获取指定的钱包账户
	 * @param address
	 * @return
	 */
	public Optional<Account> getAccount(String address);

	/**
	 * 设置挖矿账户
	 * @param account
	 */
	public boolean putCoinBaseAccount(Optional<Account> account);

	/**
	 * 获取挖矿账户
	 * @return
	 */
	public Optional<Account> getCoinBaseAccount();

	/**
	 * 往数据库添加|更新一条数据
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean put(Object key, Object value);

	/**
	 * 获取某一条指定的数据
	 * @param key
	 * @return
	 */
	public Optional<Object> get(Object key);

	/**
	 * 删除一条数据
	 * @param key
	 * @return
	 */
	public boolean delete(Object key);

}
