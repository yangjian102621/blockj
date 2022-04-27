package org.rockyang.jblock.service;

import org.rockyang.jblock.base.model.Wallet;

import java.util.List;

/**
 * @author yangjian
 */
public interface WalletService {
	String WALLET_PREFIX = "/wallets/";
	String MINER_ADDR_KEY = "/wallets/miner";
	String DEFAULT_ADDR_KEY = "/wallets/default";

	boolean addWallet(Wallet wallet);

	List<Wallet> getWallets();

	Wallet getWallet(String address);

	Wallet getMinerWallet();

	boolean setMinerWallet(Wallet wallet);

	String getDefaultWallet();

	boolean setDefaultWallet(String address);
}
