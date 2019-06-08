package org.rockyang.blockchain.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author yangjian
 * @since 19-6-8 下午1:29
 */
@Controller
public class IndexController {

	@GetMapping("/")
	public String hello() {
		return "Hello blockchain.";
	}
}
