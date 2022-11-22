package com.example.demo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseDTO<T> {		// TodoDTO �Ӹ� �ƴ϶� �ٸ� ���� DTO�� ReponseDTO�� �̿��� �� �ֵ��� Generic���� ����
	private String error;
	private List<T> data;
}