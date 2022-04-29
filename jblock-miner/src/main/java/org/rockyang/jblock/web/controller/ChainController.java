package org.rockyang.jblock.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.rockyang.jblock.base.model.Block;
import org.rockyang.jblock.base.model.Message;
import org.rockyang.jblock.service.BlockService;
import org.rockyang.jblock.service.MessageService;
import org.rockyang.jblock.web.vo.JsonVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * chain api handler
 */
@RestController
@RequestMapping("/chain")
public class ChainController {
	private final BlockService blockService;
	private final MessageService messageService;

	public ChainController(BlockService blockService, MessageService messageService)
	{
		this.blockService = blockService;
		this.messageService = messageService;
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

	@GetMapping("/getMessage")
	public JsonVo getMessage(@RequestBody JSONObject params)
	{
		String cid = params.getString("cid");
		Message message = messageService.getMessage(cid);
		return JsonVo.success().setData(message);
	}

	@GetMapping("/sendMessage")
	public JsonVo sendMessage(@RequestBody JSONObject params) throws Exception
	{
		String from = params.getString("from");
		String to = params.getString("to");
		BigDecimal value = params.getBigDecimal("amount");
		String data = params.getString("param");
		
		Preconditions.checkNotNull(from, "must pass parameter: from");
		Preconditions.checkNotNull(to, "must pass parameter: to");
		Preconditions.checkArgument(value.compareTo(BigDecimal.ZERO) <= 0, "the value of send amount must > 0");

		String cid = messageService.sendMessage(from, to, value, data);
		return JsonVo.success().setData(cid);
	}
}
