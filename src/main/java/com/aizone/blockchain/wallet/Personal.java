package com.aizone.blockchain.wallet;

import com.aizone.blockchain.db.DBAccess;
import com.aizone.blockchain.encrypt.WalletUtils;
import com.aizone.blockchain.event.NewAccountEvent;
import com.aizone.blockchain.net.ApplicationContextProvider;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.KeyPair;

/**
 * 账户控制工具类, 锁定，解锁等操作
 * @author yangjian
 * @since 18-4-6
 */
@Component
public class Personal {

	@Autowired
	private DBAccess dbAccess;

	/**
	 * 创建一个默认账户
	 * @return
	 */
	public Account newAccount() throws Exception {

		KeyPair keyPair = WalletUtils.generateKeyPair();
		Account account = new Account(keyPair.getPublic().getEncoded());
		//不存储私钥
		dbAccess.putAccount(account);
		//发布同步账号事件
		ApplicationContextProvider.publishEvent(new NewAccountEvent(account));
		account.setPrivateKey(WalletUtils.privateKeyToString(keyPair.getPrivate()));
		//如果没有发现挖矿账号, 则优先创建挖矿账号
		Optional<Account> coinBaseAccount = dbAccess.getCoinBaseAccount();
		if (!coinBaseAccount.isPresent()) {
			dbAccess.putCoinBaseAccount(account);
		}
		return account;
	}
}
