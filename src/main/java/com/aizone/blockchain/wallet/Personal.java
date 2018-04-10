package com.aizone.blockchain.wallet;

import com.aizone.blockchain.encrypt.WalletUtils;

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

		return account;
	}
}
