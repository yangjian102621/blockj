//package org.rockyang.jblock.web.controller.api;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.rockyang.jblock.db.Datastore;
//import org.rockyang.jblock.utils.JsonVo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * @author yangjian
// * @since 18-4-8
// */
//@RestController
//@RequestMapping("/api/account")
//@Api(tags = "Account API", description = "账户相关的 API")
//public class AccountController {
//
//	@Autowired
//	private Datastore dataStore;
//
//	/**
//	 * 创建账户
//	 * @return
//	 */
//	@ApiOperation(value="创建一个新的钱包账户")
//	@GetMapping("/new_account")
//	public JsonVo newAccount() throws Exception
//	{
////		ECKeyPair keyPair = Keys.createEcKeyPair();
////		Wallet wallet = this.wallet.newAccount(keyPair);
////		WalletVo vo = new WalletVo();
////		BeanUtils.copyProperties(wallet, vo);
////		return new JsonVo(JsonVo.CODE_SUCCESS,
////				"New account created, please remember your Address and Private Key.",
////				vo);
//		return JsonVo.success();
//	}
//
//	/**
//	 * 获取挖矿账号
//	 * @return
//	 */
//	@ApiOperation(value="获取挖矿钱包账号", notes = "获取挖矿钱包账号信息，包括地址，私钥，余额等信息")
//	@GetMapping("/get_miner_address")
//	public JsonVo getMinerAddress()
//	{
////		Optional<Account> minerAccount = dataStore.getMinerAccount();
////		JsonVo success = JsonVo.success();
////		if (minerAccount.isPresent()) {
////			success.setItem(minerAccount.get());
////		} else {
////			success.setMessage("Miner account is not created");
////		}
//		return JsonVo.success();
//	}
//
//	/**
//	 * 列出所有的账号
//	 * @return
//	 */
//	@GetMapping("/list")
//	@ApiOperation(value = "获取当前节点所有钱包账户")
//	public JsonVo getAllAccounts()
//	{
////		List<Account> accounts = dataStore.getAllAccounts();
////		JsonVo success = JsonVo.success();
////		success.setItem(accounts);
//		return JsonVo.success();
//	}
//}
