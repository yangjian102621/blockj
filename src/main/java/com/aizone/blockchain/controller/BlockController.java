package com.aizone.blockchain.controller;

import com.aizone.blockchain.core.Block;
import com.aizone.blockchain.core.BlockChain;
import com.aizone.blockchain.core.Transaction;
import com.aizone.blockchain.utils.JsonVo;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yangjian
 * @since 2018-04-07 上午10:50.
 */
@RestController
@RequestMapping("/chain")
public class BlockController {
	/**
	 * 收款人私钥，测试数据
	 */
	private static final String SENDER_PRIVATE_KEY = "";
	/**
	 * 收款人公钥，测试数据
	 */
	private static final String SENDER_PUBLIC_KEY = "";

	@Autowired
	private BlockChain blockChain;

	@GetMapping({"", "/", "index"})
	public JsonVo index(HttpServletRequest request) {
		return JsonVo.success();
	}

	/**
	 * 挖矿
	 * @param request
	 * @return
	 */
	@GetMapping("/mine")
	public JsonVo mine(HttpServletRequest request) {

		Block block = blockChain.mining();
		JsonVo vo = new JsonVo();
		vo.setCode(JsonVo.CODE_SUCCESS);
		vo.setMessage("Create a new block");
		vo.setItem(block);
		return vo;
	}

	/**
	 * 浏览区块
	 * @param request
	 * @return
	 */
	@GetMapping("/block/view")
	public JsonVo viewChain(HttpServletRequest request) {
		JsonVo success = JsonVo.success();
		success.setItem(blockChain);
		return success;

	}

	/**
	 * 发送交易
	 * @param transaction
	 * @return
	 */
	@PostMapping("/transactions/new")
	public JsonVo sendTransaction(@RequestBody Transaction transaction) {
		Preconditions.checkNotNull(transaction.getSender(), "sender is needed.");
		Preconditions.checkNotNull(transaction.getRecipient(), "recipient is needed.");
		Preconditions.checkNotNull(transaction.getAmount(), "Amount is needed.");
		return JsonVo.success();
	}

	/**
	 * 添加节点
	 * @param request
	 * @return
	 */
	@PostMapping("/node/add")
	public JsonVo addNode(HttpServletRequest request) {
		return JsonVo.success();
	}

	/**
	 * 手动同步节点区块
	 * @param request
	 * @return
	 */
	@GetMapping("node/sync")
	public JsonVo sync(HttpServletRequest request) {
		return JsonVo.success();
	}

}
