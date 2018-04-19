package com.aizone.blockchain.web.controller;

import com.aizone.blockchain.web.vo.TransactionVo;
import com.aizone.blockchain.core.Block;
import com.aizone.blockchain.core.BlockChain;
import com.aizone.blockchain.core.Transaction;
import com.aizone.blockchain.db.DBUtils;
import com.aizone.blockchain.utils.JsonVo;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.springframework.beans.BeanUtils;
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
	public JsonVo mine(HttpServletRequest request) throws Exception {

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

		Optional<Block> block = DBUtils.getLastBlock();
		JsonVo success = JsonVo.success();
		if (block.isPresent()) {
			success.setItem(block.get());
		}
		return success;

	}

	/**
	 * 发送交易
	 * @param txVo
	 * @return
	 */
	@PostMapping("/transactions/new")
	public JsonVo sendTransaction(@RequestBody TransactionVo txVo) throws Exception {
		Preconditions.checkNotNull(txVo.getSender(), "Sender is needed.");
		Preconditions.checkNotNull(txVo.getRecipient(), "Recipient is needed.");
		Preconditions.checkNotNull(txVo.getAmount(), "Amount is needed.");
		Preconditions.checkNotNull(txVo.getPrivateKey(), "Private Key is needed.");
		Transaction tx = new Transaction();
		BeanUtils.copyProperties(txVo, tx);
		Transaction transaction = blockChain.sendTransaction(tx, txVo.getPrivateKey());
		JsonVo success = JsonVo.success();
		success.setItem(transaction);
		return success;
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
	 * 测试节点心跳，判断是会否可用节点
	 * @param request
	 * @return
	 */
	@GetMapping("/ping")
	public JsonVo ping(HttpServletRequest request) {
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
