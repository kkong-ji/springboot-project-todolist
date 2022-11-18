package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController				// RestAPI를 구현하므로 명시해줌
@RequestMapping("test")		// 리소스
public class TestController {
	
	@GetMapping
	public String testController() {
		return "Hello World!";
	}
	
	@GetMapping("/testGetMapping")
	public String testControllerWithPath() {
		return "Hello wordl! testGetMapping";
	}
	
	@GetMapping("/{id}")			// PathVariable을 이용해 매개변수 전달
	public String testControllerWithPathVariables(@PathVariable(required = false) int id) {
		return "Hello World! ID " + id;
	}
	
	@GetMapping("/testRequestParam")  // RequestParam을 이용해 매개변수 전달
	public String testControllerRequestParam(@RequestParam(required = false) int id) {
		return "Hello World! ID " + id;
	}
	
}
