package org.rockyang.jblock.web.controller;

import org.rockyang.jblock.base.model.Account;
import org.rockyang.jblock.base.model.Wallet;
import org.rockyang.jblock.service.AccountService;
import org.rockyang.jblock.service.WalletService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yangjian
 */
@RestController
@RequestMapping("/wallet")
public class WalletController {

	private final WalletService walletService;
	private final AccountService accountService;

	public WalletController(WalletService walletService, AccountService accountService)
	{
		this.walletService = walletService;
		this.accountService = accountService;
	}

	@GetMapping("/new")
	public String newWallet() throws Exception
	{
		Wallet wallet = new Wallet();
		walletService.addWallet(wallet);
		return wallet.getAddress();
	}

	@GetMapping("/new/mnemonic")
	public String newMnemonicWallet()
	{
		throw new RuntimeException("Not implemented");
	}

	@GetMapping("/list")
	public List<Account> walletList()
	{
		// @Note: we only return the local wallet account infos
		// should not to export the private key
		List<Wallet> wallets = walletService.getWallets();
		List<Account> accounts = new ArrayList<>();
		for (Wallet w : wallets) {
			Account account = accountService.getAccount(w.getAddress());
			if (account == null) {
				continue;
			}

			accounts.add(account);
		}
		return accounts;
	}
}
