package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.Data;

@Data
public class TestRequestBodyDTO {    // ���� ��ȯ�ϰ��� �ϴ� ���ҽ��� ������ �� RequestBody ���
	private int id;
	private String message;

	@GetMapping("/testRequestBody")
	public String testControllerRequest(@RequestBody TestRequestBodyDTO testRequestBodyDTO) {
		return "Hello World! ID " + testRequestBodyDTO.getId() + " Message : " + testRequestBodyDTO.getMessage();
	}
	
	@GetMapping("/testResponseBody")
	public ResponseDTO<String> testControllerResponseBody() {
		List<String> list = new ArrayList<>();
		list.add("Hello World! I'm ResponseDTO");
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		return response;
	}
	
	@GetMapping("/testResponseEntity")
	public ResponseEntity<?> testControllerResponseEntity() {
		List<String> list = new ArrayList<>();
		list.add("Hello World! I'm ResponseEntity. And you got 400!");
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		// http status�� 400���� ����
		return ResponseEntity.badRequest().body(response);
	}
}
