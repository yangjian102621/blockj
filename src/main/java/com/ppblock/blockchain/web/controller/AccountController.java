package com.ppblock.blockchain.web.controller;

import com.ppblock.blockchain.crypto.ECKeyPair;
import com.ppblock.blockchain.crypto.Keys;
import com.ppblock.blockchain.utils.JsonVo;
import com.ppblock.blockchain.account.Account;
import com.google.common.base.Optional;
import com.ppblock.blockchain.account.Personal;
import com.ppblock.blockchain.db.DBAccess;
import com.ppblock.blockchain.web.vo.AccountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author yangjian
 * @since 18-4-8
 */
@RestController
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private Personal personal;
	@Autowired
	private DBAccess dbAccess;

	/**
	 * 创建账户
	 * @param request
	 * @return
	 */
	@PostMapping("/new")
	public JsonVo newAccount(HttpServletRequest request) throws Exception {

		ECKeyPair keyPair = Keys.createEcKeyPair();
		Account account = personal.newAccount(keyPair);
		AccountVo vo = (AccountVo) account;
		vo.setPrivateKey(keyPair.exportPrivateKey());
		return new JsonVo(JsonVo.CODE_SUCCESS, "New account created, please remember your Address and Private Key.",
				vo);
	}

	/**
	 * 获取挖矿账号
	 * @param request
	 * @return
	 */
	@GetMapping("/coinbase")
	public JsonVo coinbase(HttpServletRequest request) {

		Optional<Account> coinBaseAccount = dbAccess.getCoinBaseAccount();
		JsonVo success = JsonVo.success();
		if (coinBaseAccount.isPresent()) {
			success.setItem(coinBaseAccount.get());
		} else {
			success.setMessage("CoinBase Account is not created");
		}
		return success;
	}

	/**
	 * 列出所有的账号
	 * @param request
	 * @return
	 */
	@GetMapping("/list")
	public JsonVo listAccounts(HttpServletRequest request) {

		List<Account> accounts = dbAccess.listAccounts();
		JsonVo success = JsonVo.success();
		success.setItem(accounts);
		return success;
	}
}
