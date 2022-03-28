package org.rockyang.jblock.chain.service;

import org.rockyang.jblock.chain.Wallet;

import java.util.List;

/**
 * @author yangjian
 */
public interface WalletService {

	void addWallet(Wallet wallet);
	List<Wallet> getWalletList();
	Wallet getWallet(String address);
	Wallet getMinerWallet();
}
