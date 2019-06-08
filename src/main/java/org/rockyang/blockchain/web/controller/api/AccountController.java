package org.rockyang.blockchain.web.controller.api;

import com.google.common.base.Optional;
import org.rockyang.blockchain.account.Account;
import org.rockyang.blockchain.account.Personal;
import org.rockyang.blockchain.crypto.ECKeyPair;
import org.rockyang.blockchain.crypto.Keys;
import org.rockyang.blockchain.db.DBAccess;
import org.rockyang.blockchain.utils.JsonVo;
import org.rockyang.blockchain.web.vo.AccountVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yangjian
 * @since 18-4-8
 */
@RestController
@RequestMapping("/api/account")
public class AccountController {

	@Autowired
	private Personal personal;
	@Autowired
	private DBAccess dbAccess;

	/**
	 * 创建账户
	 * @return
	 */
	@GetMapping("/new_account")
	public JsonVo newAccount() throws Exception
	{
		ECKeyPair keyPair = Keys.createEcKeyPair();
		Account account = personal.newAccount(keyPair);
		AccountVo vo = new AccountVo();
		BeanUtils.copyProperties(account, vo);
		vo.setPrivateKey(keyPair.exportPrivateKey());
		return new JsonVo(JsonVo.CODE_SUCCESS,
				"New account created, please remember your Address and Private Key.",
				vo);
	}

	/**
	 * 获取挖矿账号
	 * @return
	 */
	@GetMapping("/get_miner_address")
	public JsonVo getMinerAddress()
	{
		Optional<Account> minerAccount = dbAccess.getMinerAccount();
		JsonVo success = JsonVo.success();
		if (minerAccount.isPresent()) {
			success.setItem(minerAccount.get());
		} else {
			success.setMessage("Miner account is not created");
		}
		return success;
	}

	/**
	 * 列出所有的账号
	 * @return
	 */
	@GetMapping("/list")
	public JsonVo getAllAccounts()
	{
		List<Account> accounts = dbAccess.getAllAccounts();
		JsonVo success = JsonVo.success();
		success.setItem(accounts);
		return success;
	}
}
