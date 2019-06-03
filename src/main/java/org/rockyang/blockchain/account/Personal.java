package org.rockyang.blockchain.account;

import com.google.common.base.Optional;
import org.rockyang.blockchain.crypto.ECKeyPair;
import org.rockyang.blockchain.db.DBAccess;
import org.rockyang.blockchain.event.NewAccountEvent;
import org.rockyang.blockchain.net.ApplicationContextProvider;
import org.rockyang.blockchain.crypto.ECKeyPair;
import org.rockyang.blockchain.db.DBAccess;
import org.rockyang.blockchain.event.NewAccountEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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
	 * @param keyPair
	 * @return
	 */
	public Account newAccount(ECKeyPair keyPair) {

		Account account = new Account(keyPair.getAddress(), BigDecimal.ZERO);
		//存储账户
		dbAccess.putAccount(account);
		//发布同步账号事件
		ApplicationContextProvider.publishEvent(new NewAccountEvent(account));
		//如果没有发现挖矿账号, 则优先创建挖矿账号
		Optional<Account> coinBaseAccount = dbAccess.getCoinBaseAccount();
		if (!coinBaseAccount.isPresent()) {
			dbAccess.putCoinBaseAccount(account);
		}
		return account;
	}
}
