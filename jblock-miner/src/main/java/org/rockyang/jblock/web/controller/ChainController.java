package org.rockyang.jblock.web.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.rockyang.jblock.base.model.Block;
import org.rockyang.jblock.service.BlockService;
import org.rockyang.jblock.web.vo.JsonVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * chain api handler
 */
@RestController
@RequestMapping("/chain")
public class ChainController {
	private final BlockService blockService;

	public ChainController(BlockService blockService)
	{
		this.blockService = blockService;
	}

	@GetMapping("/head")
	public JsonVo head()
	{
		long head = blockService.chainHead();
		if (head < 0) {
			return JsonVo.fail().setMessage("Invalid chain head found.");
		}
		return JsonVo.success().setData(head);
	}

	@GetMapping("/getBlock")
	public JsonVo getBlock(@RequestBody JSONObject params)
	{
		String hash = params.getString("hash");
		if (!StringUtils.isEmpty(hash)) {
			Block block = blockService.getBlock(hash);
			return JsonVo.success().setData(block);
		}
		long height = params.getLongValue("height");
		Block block = blockService.getBlockByHeight(height);
		return JsonVo.success().setData(block);
	}
}
