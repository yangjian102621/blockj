package org.rockyang.jblock.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import org.rockyang.jblock.base.model.Message;
import org.rockyang.jblock.service.MessageService;
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

	public MessageController(MessageService messageService)
	{
		this.messageService = messageService;
	}

	@GetMapping("/get")
	public Message getMessage(@RequestBody JSONObject params)
	{
		String cid = params.getString("cid");
		Preconditions.checkNotNull(cid, "must pass the message cid");
		return messageService.getMessage(cid);
	}

	@GetMapping("/send")
	public String sendMessage(@RequestBody JSONObject params) throws Exception
	{
		String from = params.getString("from");
		String to = params.getString("to");
		BigDecimal value = params.getBigDecimal("amount");
		String data = params.getString("param");

		Preconditions.checkNotNull(from, "must pass the from address");
		Preconditions.checkNotNull(to, "must pass the to address");
		Preconditions.checkArgument(value.compareTo(BigDecimal.ZERO) <= 0, "the value of send amount must > 0");

		return messageService.sendMessage(from, to, value, data);
	}
}
