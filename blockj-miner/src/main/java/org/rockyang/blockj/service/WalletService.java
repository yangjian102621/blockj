package org.rockyang.blockj.service;

import org.rockyang.blockj.base.model.Wallet;

import java.util.List;

/**
 * @author yangjian
 */
public interface WalletService
{
    String WALLET_PREFIX = "/wallets/";
    String MINER_ADDR_KEY = "/wallet/miner";
    String DEFAULT_ADDR_KEY = "/wallet/default";

    boolean addWallet(Wallet wallet);

    List<Wallet> getWallets();

    Wallet getWallet(String address);

    Wallet getMinerWallet();

    boolean setMinerWallet(Wallet wallet);

    Wallet getDefaultWallet();

    boolean setDefaultWallet(String address);

    boolean deleteWallet(String address);
}
