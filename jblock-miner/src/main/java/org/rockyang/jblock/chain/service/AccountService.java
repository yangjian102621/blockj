package org.rockyang.jblock.chain.service;

import org.rockyang.jblock.base.model.Account;

import java.math.BigDecimal;

/**
 * @author yangjian
 */
public interface AccountService {
	BigDecimal getBalance(String address);

	void addBalance(String address, BigDecimal value);

	void subBalance(String address, BigDecimal value);

	void addMessageNonce(String address, long value);

	void setAccount(Account account);

	Account getAccount(String address);
}
