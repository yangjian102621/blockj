package com.aizone.blockchain.wallet;

import com.aizone.blockchain.encrypt.WalletUtils;
import com.aizone.blockchain.utils.RocksDBUtils;
import com.google.common.base.Optional;
import org.rocksdb.RocksDBException;

import java.security.KeyPair;

/**
 * 账户控制工具类, 锁定，解锁等操作
 * @author yangjian
 * @since 18-4-6
 */
public class Personal {

	/**
	 * 创建一个默认账户
	 * @return
	 */
	public static Account newAccount() throws Exception {
		KeyPair keyPair = WalletUtils.generateKeyPair();
		Account account = new Account(keyPair.getPublic().getEncoded());
		//如果不存在挖矿账户，则自动设置该账户为挖矿账户
		Optional<Account> coinbaseAccount = RocksDBUtils.getInstance().getCoinbaseAccount();
		if (!coinbaseAccount.isPresent()) {
			//存储私钥
			account.setPrivateKey(WalletUtils.privateKeyToString(keyPair.getPrivate()));
			RocksDBUtils.getInstance().putCoinbaseAccount(Optional.of(account));
		} else {
			RocksDBUtils.getInstance().putAccount(account);
			account.setPrivateKey(WalletUtils.privateKeyToString(keyPair.getPrivate()));
		}
		return account;
	}

	/**
	 * 锁定账户
	 * @param address
	 * @param password
	 * @throws RocksDBException
	 */
	public static void lockAccount(String address, String password) throws RocksDBException {
		Account account = RocksDBUtils.getInstance().getAccount(address);
		account.setLocked(true);
		RocksDBUtils.getInstance().putAccount(account);
	}

	/**
	 * 解锁账户
	 * @param address
	 * @param password
	 * @throws RocksDBException
	 */
	public static void unLockAccount(String address, String password) throws RocksDBException {
		Account account = RocksDBUtils.getInstance().getAccount(address);
		account.setLocked(false);
		RocksDBUtils.getInstance().putAccount(account);
	}
}
