package org.rockyang.jblock.service.impl;

import org.rockyang.jblock.base.model.Wallet;
import org.rockyang.jblock.base.store.Datastore;
import org.rockyang.jblock.service.WalletService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author yangjian
 */
@Service
public class WalletServiceImpl implements WalletService {

	private final Datastore datastore;

	public WalletServiceImpl(Datastore datastore)
	{
		this.datastore = datastore;
	}

	@Override
	public boolean addWallet(Wallet wallet)
	{
		return datastore.put(WALLET_PREFIX + wallet.getAddress(), wallet);
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
	public boolean setMinerWallet(Wallet wallet)
	{
		if (!addWallet(wallet)) {
			return false;
		}
		return datastore.put(MINER_ADDR_KEY, wallet.getAddress());
	}

	@Override
	public String getDefaultWallet()
	{
		return (String) datastore.get(DEFAULT_ADDR_KEY).orElse(null);
	}

	@Override
	public boolean setDefaultWallet(String address)
	{
		return datastore.put(DEFAULT_ADDR_KEY, address);
	}
}
