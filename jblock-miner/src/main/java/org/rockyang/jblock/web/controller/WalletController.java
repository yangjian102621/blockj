package org.rockyang.jblock.web.controller;

import org.rockyang.jblock.base.model.Wallet;
import org.rockyang.jblock.service.WalletService;
import org.rockyang.jblock.web.vo.JsonVo;
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

	public WalletController(WalletService walletService)
	{
		this.walletService = walletService;
	}

	@GetMapping("/new")
	public JsonVo newWallet() throws Exception
	{
		Wallet wallet = new Wallet();
		walletService.addWallet(wallet);
		return JsonVo.success().setData(wallet.getAddress());
	}

	@GetMapping("/new/mnemonic")
	public JsonVo newMnemonicWallet()
	{
		return JsonVo.success().setData("Not implemented");
	}

	@GetMapping("/list")
	public JsonVo walletList()
	{
		List<Wallet> wallets = walletService.getWallets();
		return JsonVo.success().setData(wallets);
	}
}
