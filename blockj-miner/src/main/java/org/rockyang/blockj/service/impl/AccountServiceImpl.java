package org.rockyang.blockj.service.impl;

import org.rockyang.blockj.base.model.Account;
import org.rockyang.blockj.base.store.Datastore;
import org.rockyang.blockj.service.AccountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author yangjian
 */
@Service
public class AccountServiceImpl implements AccountService {

	private final Datastore datastore;

	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private final Lock readLock = rwl.readLock();
	private final Lock writeLock = rwl.writeLock();

	public AccountServiceImpl(Datastore datastore)
	{
		this.datastore = datastore;
	}

	@Override
	public BigDecimal getBalance(String address)
	{
		readLock.lock();
		Optional<Object> o = datastore.get(ACCOUNT_PREFIX + address);
		readLock.unlock();
		return o.map(a -> ((Account) a).getBalance()).orElse(BigDecimal.ZERO);
	}

	@Override
	public boolean addBalance(String address, BigDecimal value)
	{
		writeLock.lock();
		try {
			Account account = getAccount(address);
			if (account == null) {
				return false;
			}
			account.setBalance(account.getBalance().add(value));
			return setAccount(account);
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public boolean subBalance(String address, BigDecimal value)
	{
		writeLock.lock();
		try {
			Account account = getAccount(address);
			if (account == null) {
				return false;
			}
			account.setBalance(account.getBalance().subtract(value));
			return setAccount(account);
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public boolean addMessageNonce(String address, long value)
	{
		writeLock.lock();
		Account account = getAccount(address);
		account.setMessageNonce(account.getMessageNonce() + value);
		boolean r = setAccount(account);
		writeLock.unlock();
		return r;
	}

	@Override
	public boolean setAccount(Account account)
	{
		writeLock.lock();
		try {
			// @Note: we should not to set the balance directly, it should ONLY to be add or subtract
			Account old = getAccount(account.getAddress());
			if (old != null) {
				account.setBalance(old.getBalance());
			}
			return datastore.put(ACCOUNT_PREFIX + account.getAddress(), account);
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public Account getAccount(String address)
	{
		readLock.lock();
		Optional<Object> o = datastore.get(ACCOUNT_PREFIX + address);
		readLock.unlock();
		return (Account) o.orElse(null);
	}
}
