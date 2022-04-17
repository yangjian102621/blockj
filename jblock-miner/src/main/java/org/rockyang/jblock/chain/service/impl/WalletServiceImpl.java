package org.rockyang.jblock.chain.service.impl;

import org.rockyang.jblock.base.model.Wallet;
import org.rockyang.jblock.base.store.Datastore;
import org.rockyang.jblock.chain.service.WalletService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author yangjian
 */
@Service
public class WalletServiceImpl implements WalletService {

	private final String WALLET_PREFIX = "/wallets/";
	private final String MINER_ADDR_KEY = "/wallets/miner";
	private final String DEFAULT_ADDR_KEY = "/wallets/default";
	private final Datastore datastore;

	public WalletServiceImpl(Datastore datastore)
	{
		this.datastore = datastore;
	}

	@Override
	public void addWallet(Wallet wallet)
	{
		datastore.put(WALLET_PREFIX + wallet.getAddress(), wallet);
	}

	@Override
	public List<Wallet> getWallets()
	{
		return datastore.search(WALLET_PREFIX);
	}

	@Override
	public Wallet getWallet(String address)
	{
		return (Wallet) datastore.get(WALLET_PREFIX + address).orElse(null);
	}

	@Override
	public Wallet getMinerWallet()
	{
		Optional<Object> o = datastore.get(MINER_ADDR_KEY);
		return o.map(address -> getWallet(String.valueOf(address))).orElse(null);
	}

	@Override
	public void setMinerWallet(Wallet wallet)
	{
		addWallet(wallet);
		datastore.put(MINER_ADDR_KEY, wallet.getAddress());
	}

	@Override
	public String getDefaultWallet()
	{
		return (String) datastore.get(DEFAULT_ADDR_KEY).orElse(null);
	}

	@Override
	public void setDefaultWallet(String address)
	{
		datastore.put(DEFAULT_ADDR_KEY, address);
	}
}
