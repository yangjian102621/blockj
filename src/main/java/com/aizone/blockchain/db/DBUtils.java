package com.aizone.blockchain.db;

import com.aizone.blockchain.core.Block;
import com.aizone.blockchain.net.base.Node;
import com.aizone.blockchain.wallet.Account;
import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库工具类
 * @author yangjian
 * @since 18-4-13
 */
public class DBUtils {

	private static RocksDBAccess rocksDBAccess;
	/**
	 * 客户端节点列表存储 key
	 */
	private static final String CLIENT_NODES_LIST_KEY = "client-node-list";

	static {
		rocksDBAccess = new RocksDBAccess();
	}

	/**
	 * 更新最新一个区块的Hash值
	 * @param lastBlock
	 * @return
	 */
	public static boolean putLastBlockIndex(Object lastBlock) {
		return rocksDBAccess.putLastBlockIndex(lastBlock);
	}

	/**
	 * 获取最新一个区块的Hash值
	 * @return
	 */
	public static Optional<Object> getLastBlockIndex() {
		return rocksDBAccess.getLastBlockIndex();
	}

	/**
	 * 保存区块
	 * @param block
	 * @return
	 */
	public static boolean putBlock(Block block) {
		return rocksDBAccess.putBlock(block);
	}

	/**
	 * 获取指定的区块, 根据区块高度去获取
	 * @param blockIndex
	 * @return
	 */
	public static Optional<Block> getBlock(Object blockIndex) {
		return rocksDBAccess.getBlock(blockIndex.toString());
	}

	/**
	 * 获取最后（最大高度）一个区块
	 * @return
	 */
	public static Optional<Block> getLastBlock() {
		return rocksDBAccess.getLastBlock();
	}

	/**
	 * 添加一个钱包账户
	 * @param account
	 */
	public static boolean putAccount(Account account) {
		return rocksDBAccess.putAccount(account);
	}

	/**
	 * 获取指定的钱包账户
	 * @param address
	 * @return
	 */
	public static Optional<Account> getAccount(String address) {
		return rocksDBAccess.getAccount(address);
	}

	/**
	 * 设置挖矿账户地址
	 * @param address
	 */
	public static boolean putCoinBaseAddress(String address) {
		return rocksDBAccess.putCoinBaseAddress(address);
	}

	/**
	 * 获取挖矿账户
	 * @return
	 */
	public static Optional<Account> getCoinBaseAccount() {
		return rocksDBAccess.getCoinBaseAccount();
	}

	/**
	 * 设置挖矿账号
	 * @param account
	 * @return
	 */
	public static boolean putCoinBaseAccount(Optional<Account> account) {
		if(account.isPresent()) {
			rocksDBAccess.putCoinBaseAddress(account.get().getAddress());
			return rocksDBAccess.putAccount(account.get());
		}
		return false;
	}

	/**
	 * 往数据库添加|更新一条数据
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean put(String key, Object value) {
		return rocksDBAccess.put(key, value);
	}

	/**
	 * 获取某一条指定的数据
	 * @param key
	 * @return
	 */
	public static Optional<Object> get(String key) {
		return rocksDBAccess.get(key);
	}

	/**
	 * 查询所有的用户
	 * @return
	 */
	public static List<Account> listAccounts() {
		return rocksDBAccess.seekByKey(RocksDBAccess.WALLETS_BUCKET_PREFIX);
	}

	/**
	 * 获取客户端节点列表
	 * @return
	 */
	public static Optional<List<Node>> getNodeList() {
		Optional<Object> nodes = rocksDBAccess.get(CLIENT_NODES_LIST_KEY);
		if (nodes.isPresent()) {
			return Optional.of((List<Node>) nodes.get());
		}
		return Optional.absent();
	}

	/**
	 * 保存客户端节点列表
	 * @param nodes
	 * @return
	 */
	public static boolean putNodeList(List<Node> nodes) {
		return rocksDBAccess.put(CLIENT_NODES_LIST_KEY, nodes);
	}

	/**
	 * 添加一个客户端节点
	 * @param node
	 * @return
	 */
	public synchronized static boolean addNode(Node node) {
		Optional<List<Node>> nodeList = getNodeList();
		if (nodeList.isPresent()) {
			nodeList.get().add(node);
			return putNodeList(nodeList.get());
		} else {
			ArrayList<Node> nodes = new ArrayList<>();
			nodes.add(node);
			return putNodeList(nodes);
		}
	}

	/**
	 * 清空所有节点
	 * @return
	 */
	public static boolean clearNodes() {
		return delete(CLIENT_NODES_LIST_KEY);
	}

	/**
	 * 删除一条数据
	 * @param key
	 * @return
	 */
	public static boolean delete(String key) {
		return rocksDBAccess.delete(key);
	}
}
