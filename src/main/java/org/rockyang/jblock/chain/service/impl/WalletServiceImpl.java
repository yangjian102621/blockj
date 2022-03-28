package org.rockyang.jblock.chain.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.rockyang.jblock.chain.Wallet;
import org.rockyang.jblock.chain.service.WalletService;
import org.rockyang.jblock.db.Datastore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author yangjian
 */
@Service
public class WalletServiceImpl implements WalletService {

	private final String WALLET_PREFIX = "/wallets/list";
	public final String MINER_ADDR_KEY= "/wallets/miner";
	private final Datastore datastore;

	public WalletServiceImpl(Datastore datastore)
	{
		this.datastore = datastore;
	}

	@Override
	public void addWallet(Wallet wallet)
	{
		List<Wallet> wallets = getWalletList();
		wallets.add(wallet);
		datastore.put(WALLET_PREFIX, wallets);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Wallet> getWalletList()
	{
		Optional<Object> o = datastore.get(WALLET_PREFIX);
		return (List<Wallet>) o.orElse(new ArrayList<Wallet>());
	}

	@Override
	public Wallet getWallet(String address)
	{
		List<Wallet> wallets = getWalletList();
		for (Wallet wallet: wallets) {
			if (StringUtils.equals(address, wallet.getAddress())) {
				return wallet;
			}
		}
		return null;
	}

	@Override
	public Wallet getMinerWallet()
	{
		Optional<Object> o = datastore.get(MINER_ADDR_KEY);
		if (o.isEmpty()) {
			return null;
		}
		return getWallet(String.valueOf(o.get()));
	}
}
