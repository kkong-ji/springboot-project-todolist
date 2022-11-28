package com.example.demo.dto;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseDTO<T> {		// TodoDTO 뿐만 아니라 다른 모델의 DTO도 ReponseDTO을 이용할 수 있도록 Generic으로 선언
	private String error;
	private List<T> data;
	
}
