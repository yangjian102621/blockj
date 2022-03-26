package org.rockyang.jblock.web.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.rockyang.jblock.chain.Chain;
import org.rockyang.jblock.chain.MessagePool;
import org.rockyang.jblock.conf.AppConfig;
import org.rockyang.jblock.db.Datastore;
import org.rockyang.jblock.utils.JsonVo;
import org.rockyang.jblock.web.vo.req.TransactionVo;
import org.springframework.web.bind.annotation.*;

/**
 * @author yangjian
 */
@RestController
@RequestMapping("/api/transaction")
@Api(tags = "Transaction API", value = "交易相关 API")
public class TransactionController {

	private final Datastore dataStore;
	private final Chain chain;
	private final AppConfig appConfig;
	private final MessagePool messagePool;

	public TransactionController(Datastore dataStore, Chain chain, AppConfig appConfig, MessagePool messagePool)
	{
		this.dataStore = dataStore;
		this.chain = chain;
		this.appConfig = appConfig;
		this.messagePool = messagePool;
	}

	/**
	 * 发送交易
	 * @param txVo
	 * @return
	 */
	@ApiOperation(value="发送交易", notes="发起一笔资金交易")
	@ApiImplicitParam(name = "txVo", required = true, dataType = "TransactionVo")
	@PostMapping("/send_transaction")
	public JsonVo sendTransaction(@RequestBody TransactionVo txVo) throws Exception {
//		Preconditions.checkNotNull(txVo.getTo(), "Recipient is needed.");
//		Preconditions.checkNotNull(txVo.getAmount(), "Amount is needed.");
//		Preconditions.checkNotNull(txVo.getPriKey(), "Private Key is needed.");
//		Credentials credentials = Credentials.create(txVo.getPriKey());
//		// 验证余额
//		Optional<Account> account = dataStore.getAccount(credentials.getAddress());
//		if (!account.isPresent()) {
//			return JsonVo.instance(JsonVo.CODE_FAIL, "账户余额不足");
//		}
//		if (account.get().getBalance().compareTo(txVo.getAmount()) < 0) {
//			return JsonVo.instance(JsonVo.CODE_FAIL, "账户余额不足");
//		}
//		Message message = blockChain.sendTransaction(
//				credentials,
//				txVo.getTo(),
//				txVo.getAmount(),
//				txVo.getData());
//
//		//如果开启了自动挖矿，则直接自动挖矿
//		if (appConfig.isAutoMining()) {
//			blockChain.mining();
//		}
//		JsonVo success = JsonVo.success();
//		success.setItem(message.getTxHash());
//		return success;
		return JsonVo.success();
	}

	/**
	 * 根据交易哈希查询交易状态
	 * @param txHash
	 * @return
	 */
	@ApiOperation(value="查询交易状态", notes="根据交易哈希查询交易状态")
	@ApiImplicitParam(name = "txHash", value = "交易哈希", required = true, dataType = "String")
	@GetMapping("/get_transaction")
	public JsonVo getTransactionByTxHash(String txHash)
	{
//		Preconditions.checkNotNull(txHash, "txHash is needed.");
//		JsonVo success = JsonVo.success();
//		// 查询交易池
//		for (Message tx: messagePool.getTransactions()) {
//			if (txHash.equals(tx.getTxHash())) {
//				success.setItem(tx);
//				return success;
//			}
//		}
//		// 查询区块
//		Message message = dataStore.getTransactionByTxHash(txHash);
//		if (null != message) {
//			success.setItem(message);
//			return success;
//		}
//
//		success.setCode(JsonVo.CODE_FAIL);
//		return success;
		return JsonVo.success();
	}

}
