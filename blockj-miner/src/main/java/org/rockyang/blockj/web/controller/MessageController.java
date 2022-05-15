package org.rockyang.blockj.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.rockyang.blockj.base.model.Message;
import org.rockyang.blockj.base.vo.JsonVo;
import org.rockyang.blockj.chain.MessagePool;
import org.rockyang.blockj.service.MessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author yangjian
 */
@RestController
@RequestMapping("/message")
public class MessageController {

	private final MessageService messageService;
	private final MessagePool messagePool;

	public MessageController(MessageService messageService, MessagePool messagePool)
	{
		this.messageService = messageService;
		this.messagePool = messagePool;
	}

	@GetMapping("/get")
	public JsonVo getMessage(@RequestBody JSONObject params)
	{
		String cid = params.getString("cid");
		Preconditions.checkArgument(StringUtils.isEmpty(cid), "Invalid message cid");

		// 1. search message in leveldb
		// 2. search message in message pool
		Message message = messageService.getMessage(cid);
		if (message == null) {
			message = messagePool.getMessage(cid);
		}

		if (message == null) {
			return JsonVo.fail().setMessage("No message found");
		} else {
			return JsonVo.success().setData(message);
		}
	}

	@GetMapping("/send")
	public JsonVo sendMessage(@RequestBody JSONObject params) throws Exception
	{
		String from = params.getString("from");
		String to = params.getString("to");
		BigDecimal value = params.getBigDecimal("amount");
		String data = params.getString("param");

		Preconditions.checkNotNull(from, "must pass the from address");
		Preconditions.checkNotNull(to, "must pass the to address");
		Preconditions.checkArgument(value.compareTo(BigDecimal.ZERO) <= 0, "the value of send amount must > 0");

		String cid = messageService.sendMessage(from, to, value, data);
		return JsonVo.success().setData(cid);
	}
}
