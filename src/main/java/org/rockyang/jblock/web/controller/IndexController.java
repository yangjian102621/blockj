package org.rockyang.jblock.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yangjian
 * @since 19-6-8 下午1:29
 */
@Controller
public class IndexController {

	@GetMapping("/")
	@ResponseBody
	public String hello() {
		return "Hello blockchain.";
	}
}
