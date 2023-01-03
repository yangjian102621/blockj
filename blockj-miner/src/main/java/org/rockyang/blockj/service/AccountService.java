package org.rockyang.blockj.service;

import org.rockyang.blockj.base.model.Account;

import java.math.BigDecimal;

/**
 * @author yangjian
 */
public interface AccountService {
	String ACCOUNT_PREFIX = "/accounts/";
	
	BigDecimal getBalance(String address);

	void addBalance(String address, BigDecimal value);

	void subBalance(String address, BigDecimal value);

	void addMessageNonce(String address, long value);

	boolean setAccount(Account account);

	Account getAccount(String address);
}
