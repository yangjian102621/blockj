package org.rockyang.jblock.web.controller;

import org.rockyang.jblock.chain.service.WalletService;
import org.rockyang.jblock.web.vo.JsonVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yangjian
 */
@RestController
@RequestMapping("/wallet")
public class WalletController {

	private WalletService walletService;

	public WalletController(WalletService walletService)
	{
		this.walletService = walletService;
	}

	@GetMapping("/new")
	public JsonVo newWallet() throws Exception
	{
		return JsonVo.success();
	}

	@GetMapping("/list")
	public JsonVo walletList()
	{
		return JsonVo.success();
	}

	@PostMapping("/import")
	public JsonVo importWallet()
	{
		return JsonVo.success();
	}

	@PostMapping("/export")
	public JsonVo exportWallet()
	{
		return JsonVo.success();
	}
}
