package org.rockyang.jblock.web.controller;

import org.rockyang.jblock.base.model.Account;
import org.rockyang.jblock.base.model.Wallet;
import org.rockyang.jblock.base.vo.JsonVo;
import org.rockyang.jblock.service.AccountService;
import org.rockyang.jblock.service.WalletService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public JsonVo newWallet() throws Exception
	{
		Wallet wallet = new Wallet();
		walletService.addWallet(wallet);
		return JsonVo.success().setData(wallet);
	}

	@GetMapping("/new/mnemonic")
	public JsonVo newMnemonicWallet()
	{
		throw new RuntimeException("Not implemented");
	}

	@GetMapping("/list")
	public JsonVo walletList()
	{
		// @Note: we only return the local wallet infos
		// should not to export the private key
		List<Wallet> wallets = walletService.getWallets();
		if (wallets.size() == 0) {
			return JsonVo.fail().setMessage("No wallet found");
		}

		for (Wallet w : wallets) {
			Account account = accountService.getAccount(w.getAddress());
			if (account != null) {
				w.setBalance(account.getBalance());
				w.setMessageNonce(account.getMessageNonce());
			}
		}
		return JsonVo.success().setData(wallets);
	}
}
