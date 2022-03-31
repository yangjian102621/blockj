package org.rockyang.jblock.chain.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.rockyang.jblock.chain.Wallet;
import org.rockyang.jblock.chain.service.WalletService;
import org.rockyang.jblock.store.Datastore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author yangjian
 */
@Service
public class WalletServiceImpl implements WalletService {

	private final String WALLET_PREFIX = "/wallets/list";
	public final String MINER_ADDR_KEY= "/wallets/miner";
	private final Datastore datastore;

	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private final Lock readLock = rwl.readLock();
	private final Lock writeLock = rwl.writeLock();

	public WalletServiceImpl(Datastore datastore)
	{
		this.datastore = datastore;
	}

	@Override
	public void addWallet(Wallet wallet)
	{
		writeLock.lock();
		List<Wallet> wallets = getWalletList();
		wallets.add(wallet);
		datastore.put(WALLET_PREFIX, wallets);
		writeLock.unlock();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Wallet> getWalletList()
	{
		readLock.lock();
		Optional<Object> o = datastore.get(WALLET_PREFIX);
		readLock.unlock();
		return (List<Wallet>) o.orElse(new ArrayList<Wallet>());
	}

	@Override
	public Wallet getWallet(String address)
	{
		readLock.lock();
		Wallet wallet = null;
		List<Wallet> wallets = getWalletList();
		for (Wallet w: wallets) {
			if (StringUtils.equals(address, w.getAddress())) {
				wallet = w;
			}
		}
		readLock.unlock();
		return wallet;
	}

	@Override
	public Wallet getMinerWallet()
	{
		readLock.lock();
		Wallet wallet;
		Optional<Object> o = datastore.get(MINER_ADDR_KEY);
		wallet = o.map(address -> getWallet(String.valueOf(address))).orElse(null);
		readLock.unlock();
		return wallet;
	}

	@Override
	public void setMinerWallet(Wallet wallet)
	{
		writeLock.lock();
		addWallet(wallet);
		datastore.put(MINER_ADDR_KEY, wallet.getAddress());
		writeLock.unlock();
	}
}
