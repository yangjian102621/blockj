package org.rockyang.jblock.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.rockyang.jblock.base.model.Block;
import org.rockyang.jblock.service.BlockService;
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
	public long head()
	{
		long head = blockService.chainHead();
		if (head < 0) {
			throw new RuntimeException("Invalid chain head found");
		}
		return head;
	}

	@GetMapping("/getBlock")
	public Block getBlock(@RequestBody JSONObject params)
	{
		String hash = params.getString("hash");
		if (!StringUtils.isEmpty(hash)) {
			return blockService.getBlock(hash);
		}
		long height = params.getLongValue("height");
		Preconditions.checkArgument(height >= 0, "Invalid block height");
		return blockService.getBlockByHeight(height);
	}
}
