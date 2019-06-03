package org.rockyang.blockchain.db;

import com.google.common.base.Optional;
import org.rocksdb.*;
import org.rockyang.blockchain.account.Account;
import org.rockyang.blockchain.conf.RocksDbProperties;
import org.rockyang.blockchain.core.Block;
import org.rockyang.blockchain.net.base.Node;
import org.rockyang.blockchain.net.conf.TioProps;
import org.rockyang.blockchain.utils.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
	private RocksDbProperties rocksDbProperties;

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
			File directory = new File(System.getProperty("user.dir")+"/"+rocksDbProperties.getDataDir());
			if (!directory.exists()) {
				directory.mkdirs();
			}
			rocksDB = RocksDB.open(new Options().setCreateIfMissing(true), rocksDbProperties.getDataDir());
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

		Optional<Object> object = this.get(BLOCKS_BUCKET_PREFIX + String.valueOf(blockIndex));
		if (object.isPresent()) {
			return Optional.of((Block) object.get());
		}
		return Optional.absent();
	}

	@Override
	public Optional<Block> getLastBlock() {
		Optional<Object> blockIndex = getLastBlockIndex();
		if (blockIndex.isPresent()) {
			return this.getBlock(blockIndex.get().toString());
		}
		return Optional.absent();
	}

	@Override
	public boolean putAccount(Account account) {
		return this.put(WALLETS_BUCKET_PREFIX + account.getAddress(), account);
	}

	@Override
	public Optional<Account> getAccount(String address) {

		Optional<Object> object = this.get(WALLETS_BUCKET_PREFIX + address);
		if (object.isPresent()) {
			return Optional.of((Account) object.get());
		}
		return Optional.absent();
	}

	@Override
	public boolean putCoinBaseAddress(String address) {
		return this.put(COIN_BASE_ADDRESS, address);
	}

	@Override
	public Optional<String> getCoinBaseAddress() {
		Optional<Object> object = this.get(COIN_BASE_ADDRESS);
		if (object.isPresent()) {
			return Optional.of((String) object.get());
		}
		return Optional.absent();
	}

	@Override
	public Optional<Account> getCoinBaseAccount() {
		Optional<String> address = getCoinBaseAddress();
		if (address.isPresent()) {
			return getAccount(address.get());
		} else {
			return Optional.absent();
		}
	}

	@Override
	public boolean putCoinBaseAccount(Account account) {

		putCoinBaseAddress(account.getAddress());
		return putAccount(account);
	}

	@Override
	public Optional<List<Node>> getNodeList() {
		Optional<Object> nodes = this.get(CLIENT_NODES_LIST_KEY);
		if (nodes.isPresent()) {
			return Optional.of((List<Node>) nodes.get());
		}
		return Optional.absent();
	}

	@Override
	public boolean putNodeList(List<Node> nodes) {
		return this.put(CLIENT_NODES_LIST_KEY, nodes);
	}

	@Override
	public synchronized boolean addNode(Node node) {
		Optional<List<Node>> nodeList = getNodeList();
		if (nodeList.isPresent()) {
			//已经存在的节点跳过
			if (nodeList.get().contains(node)) {
				return true;
			}
			//跳过自身节点
			Node self = new Node(tioProps.getServerIp(), tioProps.getServerPort());
			if (self.equals(node)) {
				return true;
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
	public void clearNodes() {
		delete(CLIENT_NODES_LIST_KEY);
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
			return Optional.absent();
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
	public List<Account> listAccounts() {

		List<Object> objects = seekByKey(WALLETS_BUCKET_PREFIX);
		List<Account> accounts = new ArrayList<>();
		for (Object o : objects) {
			accounts.add((Account) o);
		}
		return accounts;
	}

	@Override
	public void closeDB() {
		if (null != rocksDB) {
			rocksDB.close();
		}
	}
}
