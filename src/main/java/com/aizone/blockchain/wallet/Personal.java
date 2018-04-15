package com.aizone.blockchain.wallet;

import com.aizone.blockchain.encrypt.WalletUtils;
import com.aizone.blockchain.utils.DBUtils;
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
		DBUtils.putAccount(account);
		account.setPrivateKey(WalletUtils.privateKeyToString(keyPair.getPrivate()));
		return account;
	}

	/**
	 * 锁定账户
	 * @param address
	 * @param password
	 * @throws RocksDBException
	 */
	public static void lockAccount(String address, String password) throws RocksDBException {
		Optional<Account> account = DBUtils.getAccount(address);
		if (account.isPresent()) {
			account.get().setLocked(true);
			DBUtils.putAccount(account.get());
		}
	}

	/**
	 * 解锁账户
	 * @param address
	 * @param password
	 * @throws RocksDBException
	 */
	public static void unLockAccount(String address, String password) throws RocksDBException {
		Optional<Account> account = DBUtils.getAccount(address);
		if (account.isPresent()) {
			account.get().setLocked(false);
			DBUtils.putAccount(account.get());
		}
	}
}
