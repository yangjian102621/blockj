package org.rockyang.jblock.chain.service;

import org.rockyang.jblock.chain.Wallet;

import java.util.List;

/**
 * @author yangjian
 */
public interface WalletService {

	void addWallet(Wallet wallet);
	List<Wallet> getWallets();
	Wallet getWallet(String address);
	Wallet getMinerWallet();
	void setMinerWallet(Wallet wallet);
	String getDefaultWallet();
	void setDefaultWallet(String address);
}
