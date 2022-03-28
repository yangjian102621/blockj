package org.rockyang.jblock.chain.service;

import org.rockyang.jblock.chain.Account;

import java.math.BigDecimal;

/**
 * @author yangjian
 */
public interface AccountService {
	BigDecimal getBalance(String address);
	void setAccount(Account account);
}
