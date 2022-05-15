package org.rockyang.blockj.service;

import org.rockyang.blockj.base.model.Account;

import java.math.BigDecimal;

/**
 * @author yangjian
 */
public interface AccountService {
	String ACCOUNT_PREFIX = "/accounts/";
	
	BigDecimal getBalance(String address);

	boolean addBalance(String address, BigDecimal value);

	boolean subBalance(String address, BigDecimal value);

	boolean addMessageNonce(String address, long value);

	boolean setAccount(Account account);

	Account getAccount(String address);
}
