package org.rockyang.jblock.chain.service.impl;

import org.rockyang.jblock.chain.Account;
import org.rockyang.jblock.chain.service.AccountService;
import org.rockyang.jblock.store.Datastore;
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
	private final String ACCOUNT_PREFIX = "/accounts/";

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
	public void addBalance(String address, BigDecimal value)
	{
		writeLock.lock();
		Account account = getAccount(address);
		if (account == null) {
			return;
		}
		account.setBalance(account.getBalance().add(value));
		writeLock.unlock();

	}

	@Override
	public void subBalance(String address, BigDecimal value)
	{
		writeLock.lock();
		Account account = getAccount(address);
		if (account == null) {
			return;
		}
		account.setBalance(account.getBalance().subtract(value));
		writeLock.unlock();
	}

	@Override
	public void addMessageNonce(String address, long value)
	{
		writeLock.lock();
		Account account = getAccount(address);
		account.setMessageNonce(account.getMessageNonce() + value);
		writeLock.unlock();
	}

	@Override
	public void setAccount(Account account)
	{
		writeLock.lock();
		// @Note: we should not to set the balance directly, it should ONLY to be add or subtract
		Account old = getAccount(account.getAddress());
		if (old != null) {
			account.setBalance(old.getBalance());
		}
		datastore.put(ACCOUNT_PREFIX + account.getAddress(), account);
		writeLock.unlock();
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
