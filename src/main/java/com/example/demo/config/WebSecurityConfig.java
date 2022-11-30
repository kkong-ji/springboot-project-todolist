package com.example.demo.config;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.security.JwtAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;

@EnableWebSecurity
@Slf4j
public class WebSecurityConfig {
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// http ��ť��Ƽ ����
		http.cors() // WebMvcConfig���� �̹� ���������Ƿ� �⺻ cors ����
			.and()
			.csrf()	// csrf�� ���� ������� �����Ƿ� disable
				.disable()
			.httpBasic()	// token�� ����ϹǷ� basic ���� disable
				.disable()
			.sessionManagement()	// session ����� �ƴ��� ����
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()	// /�� /auth/** ��δ� ���� ���ص� ��
				.antMatchers("/", "/auth/**").permitAll()
			.anyRequest()	// /�� /auth/** �̿��� ��� ��δ� �����ؾ� ��
				.authenticated();
			
			// filter ���
			// �� ��û����
			// CorsFilter ������ �Ŀ�
			// jwtAuthenticationFilter �����Ѵ�.
			http.addFilterAfter(
					jwtAuthenticationFilter, 
					UsernamePasswordAuthenticationFilter.class);
			
			return http.build();
	};
}
