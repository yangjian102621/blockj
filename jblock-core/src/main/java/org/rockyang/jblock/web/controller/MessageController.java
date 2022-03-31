package org.rockyang.jblock.web.controller;

import org.rockyang.jblock.chain.MessagePool;
import org.rockyang.jblock.web.vo.JsonVo;
import org.springframework.web.bind.annotation.*;

/**
 * @author yangjian
 */
@RestController
@RequestMapping("/message")
public class MessageController {

	private final MessagePool messagePool;

	public MessageController(MessagePool messagePool)
	{
		this.messagePool = messagePool;
	}

	@PostMapping("/send")
	public JsonVo sendMessage(@RequestBody String message) throws Exception {
		return JsonVo.success();
	}

	@GetMapping("/getMessage")
	public JsonVo getMessage(@RequestParam String cid)
	{
		return JsonVo.success();
	}

}
