package com.aizone.blockchain.controller;

import com.aizone.blockchain.utils.JsonVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yangjian
 * @since 2018-04-07 上午10:50.
 */
@RestController
@RequestMapping("/block")
public class BlockController {

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
		return JsonVo.success();
	}

	/**
	 * 浏览区块
	 * @param request
	 * @return
	 */
	@GetMapping("/chain")
	public JsonVo chain(HttpServletRequest request) {
		return JsonVo.success();
	}

	/**
	 * 发送交易
	 * @param request
	 * @return
	 */
	@PostMapping("/transactions/new")
	public JsonVo sendTransaction(HttpServletRequest request) {
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
