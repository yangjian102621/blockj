package org.rockyang.jblock.chain.service;

import org.rockyang.jblock.base.model.Wallet;

import java.util.List;

/**
 * @author yangjian
 */
public interface WalletService {
	String WALLET_PREFIX = "/wallets/";
	String MINER_ADDR_KEY = "/wallets/miner";
	String DEFAULT_ADDR_KEY = "/wallets/default";

	void addWallet(Wallet wallet);

	List<Wallet> getWallets();

	Wallet getWallet(String address);

	Wallet getMinerWallet();

	void setMinerWallet(Wallet wallet);

	String getDefaultWallet();

	void setDefaultWallet(String address);
}
