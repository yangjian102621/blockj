package org.rockyang.blockj.web.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.rockyang.blockj.base.model.Block;
import org.rockyang.blockj.base.vo.JsonVo;
import org.rockyang.blockj.service.BlockService;
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
		Long head = blockService.chainHead();
		if (head < 0) {
			return JsonVo.fail().setMessage("Invalid chain head found");
		}

		return JsonVo.success().setData(head);
	}

	@GetMapping("/block/get")
	public JsonVo getBlock(@RequestBody JSONObject params)
	{
		Block block;
		String hash = params.getString("hash");
		if (!StringUtils.isEmpty(hash)) {
			block = blockService.getBlock(hash);
		} else {
			long height = params.getLongValue("height");
			if (height <= 0) {
				return JsonVo.fail().setMessage("Invalid block height");
			}
			block = blockService.getBlockByHeight(height);
		}
		if (block == null) {
			return JsonVo.fail().setMessage("Block not found");
		}
		return JsonVo.success().setData(block);
	}
}
