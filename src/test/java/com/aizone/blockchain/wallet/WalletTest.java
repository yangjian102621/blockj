package com.aizone.blockchain.wallet;

import com.aizone.blockchain.db.DBAccess;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 钱包服务单元测试
 * @author yangjian
 * @since 18-4-16
 */
public class WalletTest {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private DBAccess dbAccess;
	@Autowired
	private Personal personal;

	/**
	 * 创建一个钱包账户
	 * @throws Exception
	 */
	@Test
	public void newAccount() throws Exception {
		Account account = personal.newAccount();
		logger.info("New Account : {}", account);
	}

	/**
	 * 列出所有钱包账户
	 */
	@Test
	public void listAccounts() {
		//查找所有的用户
		List<Account> accounts = dbAccess.listAccounts();
		accounts.forEach(a -> logger.info("List Account ===> {}", a));
	}

}
