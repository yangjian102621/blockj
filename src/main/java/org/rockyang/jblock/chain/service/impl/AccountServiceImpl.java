package org.rockyang.jblock.chain.service.impl;

import org.rockyang.jblock.chain.Account;
import org.rockyang.jblock.chain.service.AccountService;
import org.rockyang.jblock.db.Datastore;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author yangjian
 */
@Service
public class AccountServiceImpl implements AccountService {

	private final Datastore datastore;
	private final String ACCOUNT_PREFIX = "/accounts/";

	public AccountServiceImpl(Datastore datastore)
	{
		this.datastore = datastore;
	}

	@Override
	public BigDecimal getBalance(String address)
	{
		return null;
	}

	@Override
	public void setAccount(Account account)
	{

	}
}
