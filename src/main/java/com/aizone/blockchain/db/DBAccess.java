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
	boolean putLastBlockIndex(Object lastBlock);

	/**
	 * 获取最新一个区块的Hash值
	 * @return
	 */
	Optional<Object> getLastBlockIndex();

	/**
	 * 保存区块
	 * @param block
	 * @return
	 */
	boolean putBlock(Block block);

	/**
	 * 获取指定的区块, 根据区块高度去获取
	 * @param blockIndex
	 * @return
	 */
	Optional<Block> getBlock(Object blockIndex);

	/**
	 * 获取最后（最大高度）一个区块
	 * @return
	 */
	Optional<Block> getLastBlock();

	/**
	 * 添加一个钱包账户
	 * @param account
	 * @return
	 */
	boolean putAccount(Account account);

	/**
	 * 获取指定的钱包账户
	 * @param address
	 * @return
	 */
	Optional<Account> getAccount(String address);

	/**
	 * 设置挖矿账户
	 * @param address
	 * @return
	 */
	boolean putCoinBaseAddress(String address);

	/**
	 * 获取挖矿账户地址
	 * @return
	 */
	Optional<String> getCoinBaseAddress();

	/**
	 * 获取挖矿账户
	 * @return
	 */
	Optional<Account> getCoinBaseAccount();

	/**
	 * 设置挖矿账户
	 * @param account
	 * @return
	 */
	boolean putCoinBaseAccount(Account account);

	/**
	 * 获取客户端节点列表
	 * @return
	 */
	Optional<List<Node>> getNodeList();

	/**
	 * 保存客户端节点列表
	 * @param nodes
	 * @return
	 */
	boolean putNodeList(List<Node> nodes);

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
	boolean put(String key, Object value);

	/**
	 * 获取某一条指定的数据
	 * @param key
	 * @return
	 */
	Optional<Object> get(String key);

	/**
	 * 删除一条数据
	 * @param key
	 * @return
	 */
	boolean delete(String key);

	/**
	 * 根据前缀搜索
	 * @param keyPrefix
	 * @return
	 */
	<T> List<T> seekByKey(String keyPrefix);

	/**
	 * 获取账户列表
	 * @return
	 */
	List<Account> listAccounts();

	/**
	 * 关闭数据库
	 */
	void closeDB();
}
