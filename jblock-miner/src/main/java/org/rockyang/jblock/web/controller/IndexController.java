package org.rockyang.jblock.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yangjian
 * @since 19-6-8 下午1:29
 */
@RestController
public class IndexController {

	@GetMapping("/")
	public String hello() {
		return "Hello JBlock.";
	}
}
