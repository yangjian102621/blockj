package org.rockyang.jblock.db;

import org.rocksdb.*;
import org.rockyang.jblock.account.Account;
import org.rockyang.jblock.conf.AppConfig;
import org.rockyang.jblock.core.Block;
import org.rockyang.jblock.core.Transaction;
import org.rockyang.jblock.net.base.Node;
import org.rockyang.jblock.net.conf.TioProps;
import org.rockyang.jblock.utils.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * RocksDB 操作封装
 * @author yangjian
 * @since 18-4-10
 */
@Component
public class RocksDBAccess implements DBAccess {

	static Logger logger = LoggerFactory.getLogger(RocksDBAccess.class);

	private RocksDB rocksDB;

	@Autowired
	private AppConfig appConfig;

	@Autowired
	private TioProps tioProps;

	public RocksDBAccess() {
		//
	}

	/**
	 * 初始化RocksDB
	 */
	@PostConstruct
	public void initRocksDB() {

		try {
			//如果数据库路径不存在，则创建路径
			File directory = new File(System.getProperty("user.dir")+"/"+ appConfig.getDataDir());
			if (!directory.exists()) {
				directory.mkdirs();
			}
			rocksDB = RocksDB.open(new Options().setCreateIfMissing(true), appConfig.getDataDir());
		} catch (RocksDBException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean putLastBlockIndex(Object lastBlock) {
		return this.put(LAST_BLOCK_INDEX, lastBlock);
	}

	@Override
	public Optional<Object> getLastBlockIndex() {
		return this.get(LAST_BLOCK_INDEX);
	}

	@Override
	public boolean putBlock(Block block) {
		return this.put(BLOCKS_BUCKET_PREFIX + block.getHeader().getIndex(), block);
	}

	@Override
	public Optional<Block> getBlock(Object blockIndex) {

		Optional<Object> object = this.get(BLOCKS_BUCKET_PREFIX + blockIndex);
		return object.map(o -> (Block) o);
	}

	@Override
	public Optional<Block> getLastBlock() {
		Optional<Object> blockIndex = getLastBlockIndex();
		if (blockIndex.isPresent()) {
			return this.getBlock(blockIndex.get().toString());
		}
		return Optional.empty();
	}

	@Override
	public boolean putAccount(Account account) {
		return this.put(WALLETS_BUCKET_PREFIX + account.getAddress(), account);
	}

	@Override
	public Optional<Account> getAccount(String address) {

		Optional<Object> object = this.get(WALLETS_BUCKET_PREFIX + address);
		return object.map(o -> (Account) o);
	}

	@Override
	public List<Account> getAllAccounts()
	{
		List<Object> objects = seekByKey(WALLETS_BUCKET_PREFIX);
		List<Account> accounts = new ArrayList<>();
		for (Object o : objects) {
			accounts.add((Account) o);
		}
		return accounts;
	}

	@Override
	public Optional<Account> getMinerAccount() {
		Optional<Object> object = get(MINER_ACCOUNT);
		if (object.isPresent()) {
			String minerAddress = (String) object.get();
			return getAccount(minerAddress);
		}
		return Optional.empty();
	}

	@Override
	public boolean setMinerAccount(Account account) {

		if (null != account) {
			return put(MINER_ACCOUNT, account.getAddress());
		} else {
			return false;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Optional<List<Node>> getNodeList() {
		Optional<Object> nodes = this.get(CLIENT_NODES_LIST_KEY);
		return nodes.map(o -> (List<Node>) o);
	}

	public boolean putNodeList(List<Node> nodes) {
		return this.put(CLIENT_NODES_LIST_KEY, nodes);
	}

	@Override
	public synchronized boolean addNode(Node node)
	{
		Optional<List<Node>> nodeList = getNodeList();
		if (nodeList.isPresent()) {
			//已经存在的节点跳过
			if (nodeList.get().contains(node)) {
				return false;
			}
			//跳过自身节点
			Node self = new Node(tioProps.getServerIp(), tioProps.getServerPort());
			if (self.equals(node)) {
				return false;
			}
			nodeList.get().add(node);
			return putNodeList(nodeList.get());
		} else {
			ArrayList<Node> nodes = new ArrayList<>();
			nodes.add(node);
			return putNodeList(nodes);
		}
	}

	@Override
	public boolean put(String key, Object value) {
		try {
			rocksDB.put(key.getBytes(), SerializeUtils.serialize(value));
			return true;
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.error("ERROR for RocksDB : {}", e);
			}
			return false;
		}
	}

	@Override
	public Optional<Object> get(String key) {
		try {
			return Optional.of(SerializeUtils.unSerialize(rocksDB.get(key.getBytes())));
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.error("ERROR for RocksDB : {}", e);
			}
			return Optional.empty();
		}
	}

	@Override
	public boolean delete(String key) {
		try {
			rocksDB.delete(key.getBytes());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> seekByKey(String keyPrefix) {

		ArrayList<T> ts = new ArrayList<>();
		RocksIterator iterator = rocksDB.newIterator(new ReadOptions());
		byte[] key = keyPrefix.getBytes();
		for (iterator.seek(key); iterator.isValid(); iterator.next()) {
			ts.add((T) SerializeUtils.unSerialize(iterator.value()));
		}
		return ts;
	}

	@Override
	public Transaction getTransactionByTxHash(String txHash)
	{
		Optional<Object> objectOptional = get(txHash);
		if (objectOptional.isPresent()) {
			Integer blockIndex = (Integer) objectOptional.get();
			Optional<Block> blockOptional = getBlock(blockIndex);
			if (blockOptional.isPresent()) {
				for (Transaction transaction : blockOptional.get().getBody().getTransactions()) {
					if (txHash.equals(transaction.getTxHash())) {
						return transaction;
					}
				}
			}
		}
		return null;
	}

	@Override
	public void closeDB() {
		if (rocksDB != null) {
			rocksDB.close();
		}
	}
}
