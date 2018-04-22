package com.aizone.blockchain.db;

import com.aizone.blockchain.core.Block;
import com.aizone.blockchain.net.base.Node;
import com.aizone.blockchain.wallet.Account;
import com.google.common.base.Optional;

import java.util.List;

/**
 * 数据库操作接口
 * @author yangjian
 * @since 18-4-10
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
	 * @param address
	 */
	public boolean putCoinBaseAddress(String address);

	/**
	 * 获取挖矿账户地址
	 * @return
	 */
	public Optional<String> getCoinBaseAddress();

	/**
	 * 获取挖矿账户
	 * @return
	 */
	public Optional<Account> getCoinBaseAccount();

	/**
	 * 设置挖矿账户
	 * @return
	 */
	public boolean putCoinBaseAccount(Account account);

	/**
	 * 获取客户端节点列表
	 * @return
	 */
	public Optional<List<Node>> getNodeList();

	/**
	 * 保存客户端节点列表
	 * @param nodes
	 * @return
	 */
	public boolean putNodeList(List<Node> nodes);

	/**
	 * 添加一个客户端节点
	 * @param node
	 * @return
	 */
	boolean addNode(Node node);

	/**
	 * 清空所有节点
	 */
	void clearNodes();

	/**
	 * 往数据库添加|更新一条数据
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean put(String key, Object value);

	/**
	 * 获取某一条指定的数据
	 * @param key
	 * @return
	 */
	public Optional<Object> get(String key);

	/**
	 * 删除一条数据
	 * @param key
	 * @return
	 */
	public boolean delete(String key);

	/**
	 * 根据前缀搜索
	 * @param keyPrefix
	 * @return
	 */
	public <T> List<T> seekByKey(String keyPrefix);

	/**
	 * 获取账户列表
	 * @return
	 */
	public List<Account> listAccounts();

	/**
	 * 关闭数据库
	 */
	public void closeDB();
}
