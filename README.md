## springboot-project-todolist
React, 스프링부트, aws를 공부할 목적으로 개발한 todolist  
이 README는 공부한 내용을 정리한 것입니다.  
## 1. Todo 웹 어플리케이션 기능
- Todo 생성 : + 버튼을 클릭해 Todo 아이템을 생성.
- Todo 리스트 : 생성된 아이템 목록을 화면에서 확인.
- Todo 수정 : Todo 아이템을 체크하거나 내용을 수정.
- Todo 삭제 : Todo 아이템을 삭제.
- 회원가입 : 사용자는 애플리케이션에 회원가입하고 생성된 계정을 이용해 Todo 어플리케이션에 접근.
- 로그인 : 계정을 생성한 사용자는 계정으로 로그인.
- 로그아웃 : 로그인한 사용자는 로그아웃 가능.

<br>

## 2. 레이어드 아키텍쳐
![image](https://user-images.githubusercontent.com/87354210/202670792-75ba170a-1237-4211-9a93-0250f8e6cbc6.png)
- 이 프로젝트에서는 모델과 엔티티를 한 클래스에 구현 (규모가 큰 프로젝트에서는 모델과 클래스를 따로 구현)

- `DTO (Data Transfer Object)` : 서비스가 요청을 처리하고 클라이언트로 반환할 때 사용하는 오브젝트.

- 왜 모델로 직접 전달하지 않고 DTO로 변환해서 사용?

  1. 비즈니스 로직을 `캡슐화` 하기 위해 <br>
     -> 모델은 데이터베이스 테이블 구조와 매우 유사. 따라서 노출하는 것이 꺼려짐 <br>
     -> 이 때, 캡슐화를 통해 외부 사용자에게 서비스 내부의 로직, 데이터 베이스 구조 등을 숨길 수 있음.

  2. 클라이언트가 필요한 정보를 모델이 전부 포함하지 않는 경우가 많음. <br>
    ex. 서비스 실행 중 사용자 에러가 나면 이 메시지는 어디에 포함해야하나의 문제 <br>
        모델은 서비스 로직과 관련이 없기 때문에 모델에 담기는 애매함. 이런 경우, DTO에 포함하면 된다.

<br>

## 3. REST 제약 조건
- 클라이언트 - 서버
- 상태가 없는
- 캐시되는 데이터
- 일관적인 인터페이스
- 레이어 시스템
- 코드-온 디맨드 (선택사항)

<br>

## 4. 컨트롤러 레이어 : 스프링 REST API 컨트롤러
- Http 메서드 : `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`
- 매개변수를 넘겨받는 방법
  1. `@PathVariable` : `/{id}` 와 같이 URI의 경로로 넘어오는 값을 변수로 받을 수 있음.

  2. `RequestParam`

- `@RequestBody` : 반환하고자 하는 리소스가 복잡할 때 사용. ex.오브젝트

- `@ResponseBody` : 오브젝트를 리턴할 때 사용.

- `@ResponseEntity` : HTTP 응답의 바디뿐만 아니라 여러 다른 매개변수들. 즉, `Http Status` 나 `header` 등을 조작할 때 사용.

<br>

## 5. 서비스 레이어 : 비즈니스 로직
- 컨트롤러와 퍼시스턴스(영속성 컨텍스트) 사이에서 비즈니스 로직을 수행하는 역할.  
- 서비스 레이어는 HTTP와 긴밀히 연관된 컨트롤러에서 분리돼 있고, 데이터 베이스와 긴밀히 연관된 <br>
  퍼시스턴스와도 분리돼 있음. 즉, 서비스 레이어에서는 우리가 개발하고자 하는 로직에 집중할 수 있게 됨.

<br>

## 6. 퍼시스턴스 레이어 : 스프링 데이터 JPA
이제 Todo 아이템을 데이터베이스에 저장해야함.
보통 관계형데이터베이스에 쿼리를 날린다고 하면 다음과 같은 순서를 따름.

1. 테이블을 생성.

```sql
  CREATE TABLE IF NOT EXISTS TODO (
  id VARCHAR(100) NOT NULL PRIMARY KEY,
  userId VARCHAR(100) NOT NULL,
  title VARCHAR(100) NOT NULL,
  done boolean DEFAULT false
```
아이템을 몇 개 넣었다고 가정

<br>

2. 아이템의 검색을 위해 SELECT 쿼리 작성
```sql
SELECT id, title, done
FROM Todo
WHERE id = "ff80808177";
```

<br>

이 후, 조건에 맞는 결과가 리턴.

|id|userId|title|done|
|---|---|---|---|
|ff80808177|760001|공부하기|False|
|ff80808178|87300b|운동하기|False|

<br>

그런데 문제는 이렇게 리턴된 결과를 자바 애플리케이션 내에서 사용해야하고 뿐만 아니라  
테이블 생성, 테이블에 엔트리 추가, 수정, 삭제도 모두 웹 서비스의 일부로 동작해야함.  
즉, 엔티티마다 해주어야 하는 것. <br>
이렇게 SQL을 실행하여 결과를 담아오고 이를 오브젝트로 변환하는 일련의 작업들을 `ORM(Object Relation Mapping)` 이라고 함. <br>
시간이 흐르며 이런 반복 작업을 줄일 수 있는 Hibernate 같은 ORM 프레임워크가 등장했고 더 나아가 JPA 같은 도구들이 개발됨.  

<br>

- `JPA` : 반복해서 데이터베이스 쿼리를 보내 결과를 파싱해야 하는 개발자들의 수고를 덜어줌.  
말 그대로 스펙(Specification). 즉, JPA란 자바에서 데이터베이스 접근, 저장, 관리에 필요한 스펙.
- `스프링 데이터 JPA` : JPA에 플러스 알파라고 생각하면 됨. JPA를 더 사용하기 쉽게 추상화한 것.

<br>

### Entity
- Entity 클래스는 ORM이 Entity를 보고 어떤 테이블의 어떤 필드에 매핑해야하는지 알 수 있게 해주어야함.
- 또 어떤 필드가 기본 키(PK)인지, 외래 키인지도 구분할 수 있어야함.
- 이런 사항을 모두 JPA 관련 어노테이션으로 정의하게 됨
> ❗자바 클래스를 Entity로 정의할 때 주의해야할 점

1. 클래스에는 매개변수가 없는 생성자, `NoArgsConstructor` 가 필요.
2. `Getter`/ `Setter` 가 필요.
3. 기본키(PK)를 지정.

<br>

## 7. 서비스 개발
### 생성(Create), 검색(Retrieve), 수정(Update), 삭제(Delete) API 개발

- 구현 과정 : 퍼시스턴스 -> 서비스 -> 컨트롤

<br>

### Create Todo 구현
- 퍼시스턴스(PS) 구현 : TodoRepository 사용 (JpaRepository를 상속하므로 JpaRepository가 제공하는 메서드를 사용할수 있음)  

- 서비스 구현 : create 메서드로 구현 (크게 세단계로 구성)
  - `검증` : 넘어온 엔티티가 유효한지 검사하는 로직
  - `save()` : 엔티티를 데이터베이스에 저장하고 로그를 남김.
  - `findByUserId()` : 저장된 엔티티를 포함하는 새 리스트를 리턴.  
  
- 컨트롤러 구현 : 컨트롤러는 사용자에게서 `TodoDTO`를 요청 바디 넘겨받고 이를 `TodoEntity` 로 변환해 저장.  
또 서비스에서 구현해놓은 create 메서드가 리턴하는 `TodoEntity`를 `TodoDTO`로 변환해 리턴  

![image](https://user-images.githubusercontent.com/87354210/203251362-714d01d5-d2f0-4fbb-9f99-09d320a3c7c8.png)

<br>

### Retrieve Todo 구현
- 퍼시스턴스(PS) 구현 : TodoRepository 사용

- 서비스 구현 : `findByUserId()` 메서드 사용하여 retrieve 메서드 작성

- 컨트롤러 구현 : `TodoController` 에 새 `GET` 메서드를 만들어주고 메서드 내부는 서비스 코드를 이용해 작성

<br>

### Update Todo 구현
- 퍼시스턴스(PS) 구현 : TodoRepository 사용, `save()`, `findByUserId()` 메서드 사용

- 서비스 구현 : update() 메서드 구현  
 (`Optional`과 `Lambda` 사용) 
  
- 컨트롤러 구현 :  `TodoController` 에 새 `PUT` 메서드를 만들어주고 메서드 내부는 서비스 코드를 이용해 작성

<br>

### Delete Todo 구현
- 퍼시스턴스(PS) 구현 : TodoRepository 사용, `delete()`, `findByUserId()` 메서드 사용

- 서비스 구현 : delete() 메서드 구현   
  
- 컨트롤러 구현 :  `TodoController` 에 새 `DELETE` 메서드를 만들어주고 메서드 내부는 서비스 코드를 이용해 작성

<br>

## 8. 인증 백엔드 통합
- 인증 vs 인가
- 인증 : "당신이 누구인가?" 에 대한 것.  
ex) 안전한 사람이면 집에 들어올 수 있음. 그러나, 위험한 사람이면 집에 들어올 수 없음.
- 인가 : "당신은 무엇을 할 수 있는가?" 에 대한 것.  
ex) 집에 들어온 사람은 거실을 사용할 수 있음. 그러나, 안방은 사용할 수 없음.

<br>

### Rest API 인증 기법
#### Basic 인증 기법 
- 인증을 구현하는 가장 간단한 방법.
- HTTP 요청에 아이디와 비밀번호를 같이 보냄.  
ex. HTTP 요청 헤더의 Authorization 부분에 아이디와 비밀번호를 콜론으로 이어붙인 후 인코딩한 문자열을 함께 보냄.
- 아이디와 비밀번호를 노출하기 때문에 HTTP와 사용하기엔 보안이 취약. HTTPS와 사용해야함
- 사용자를 로그아웃시킬 수 없음.
- 인증서버와 인증DB에 과부하가 걸릴 확률이 높음.

#### 토큰 기반 인증
- `Token` : 사용자를 구별할 수 있는 문자열
- 토큰은 최초 로그인 시 서버가 만들어줌.
- 아이디와 비밀번호를 매번 네트워크를 통해 전송해야할 필요가 없으므로 보안 측면에서 좀 더 안전.
- 디바이스마다 다른 토큰 생성. 유효시간을 따로 정해 관리할 수 있음.
- 임의로 로그아웃 가능

#### JSON 웹 토큰 (JWT)
- 서버에서 전자 서명된 토큰을 이용하는 방식.
- 스케일 문제를 해결할 수 있음.
- 각 파트의 필드

`<Header>`
- typ : 토큰의 타입
- alg : 토큰의 서명을 발행하는데 사용된 해시 알고리즘의 종류

`<Payload>`
- sub : 토큰의 주인을 의미. ID처럼 유일한 식별자이어야 함.
- iss : 토큰을 발행한 주체
(ex : 페이스북, 구글 등)
- iat : 토큰이 발행된 날짜와 시간
- exp : 토큰이 만료되는 시간

`<Signature>`
- 토큰을 발행한 주체 Issuer가 발행한 서명으로 토큰의 유효성 검사에 사용.

<br>

### JWT 토큰 생성과 인증

![image](https://user-images.githubusercontent.com/87354210/204417071-99a067fb-ed21-48e2-b344-5cddb17b782c.png)

- JWT에서 전자 서명이란 `{header}.{payload}` 와 `secret key` 를 이용해 해시 함수에 돌린 암호화한 결과 값
- 시크릿 키란 나만 알고 있는 문자열, 비밀번호 같은 것.

<최초의 요청 시 인증 프로세스>
- 최초 로그인 시 서버는 사용자의 아이디와 비밀번호를 서버에 저장된 아이디와 비밀번호를 비교해 인증.
- 만약 인증된 사용자인 경우, 사용자의 정보를 이용해 `{header}.{payload}` 부분을 작성하고 자신의 `secret key` 로  
`{header}.{payload}` 부분을 전자서명.
- 전자서명의 결과로 나온 값을 `{header}.{payload}.{서명}` 으로 이어붙이고 Base64로 인코딩 후 반환.

<br>

<토큰으로 리소스 접근 요청 시 프로세스>
- 요청된 토큰을 Base64로 디코딩
- 디코딩해서 얻은 JSON을 `{header}.{payload}` 와 `{서명}` 부분으로 나눔.
- 서버는 `{header}.{payload}` 와 자신이 갖고 있는 `secret key` 로 전자 서명을 만든 후,  
방금 만든 전자 서명을 HTTP 요청이 갖고 온 `{서명}` 부분과 비교해 토큰의 유효성을 검사.
- 서버가 방금 `secret key` 를 이용해 만든 전자서명과 HTTP 요청의 {서명} 부분이 일치하면 
토큰이 위조되지 않은 것.

<br>

### 스프링 시큐리티와 서블릿 필터
- 위의 그림에서처럼 API가 실행될 때마다 사용자를 인증해주는 부분을 구현해야 함.
- 스프링 시큐리티의 도움을 받아 구현.  
- `스프링 시큐리티` : 서블릿 필터의 집합.  
- `서블릿 필터` : 서블릿 실행 전에 실행되는 클래스.  
(즉, 디스패처 서블릿이 실행되기 전에 항상 실행됨.)

![image](https://user-images.githubusercontent.com/87354210/206127089-15588d51-99a5-48a6-b3df-c43aad3d0d52.png)

- 서블릿 필터는 말 그대로 구현된 로직에 따라 원하지 않는 HTTP 요청을 걸러낼 수 있음.

<br>

### 서블릿 필터는 한 개가 아닐수도 있음
![image](https://user-images.githubusercontent.com/87354210/206129887-908909b1-d1a9-4c8a-ac71-531d3e93ecc3.png)

- 기능에 따라 다른 서블릿 필터를 작성할 수 있음.
- `FilterChain` 을 이용해 연쇄적으로 순서대로 실행 가능.
- 스프링 시큐리티를 이용하면 `FilterChainProxy` 라는 필터를 서블릿 필터에 끼워줌.
  - 이 필터들이 스프링이 관리하는 스프링 빈 필터

<br>

### 스프링 시큐리티 설정
- 스프링 필터를 사용하려면?
  1. 서블릿 필터를 구현해야함.
  2. 서블릿 컨테이너에 이 서블릿 필터를 사용하려고 알려주어야함.
  
  <br>
  
  ```java
    @Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// http 시큐리티 빌더
		http.csrf().disable(); // csrf는 현재 사용하지 않으므로 disable
		http.cors() // WebMvcConfig에서 이미 설정했으므로 기본 cors 설정
			.and()
			.httpBasic()	// token을 사용하므로 basic 인증 disable
				.disable()
			.sessionManagement()	// session 기반이 아님을 선언
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()	// /와 /auth/** 경로는 인증 안해도 됨
				.antMatchers("/", "/auth/**").permitAll()
			.anyRequest()	// /와 /auth/** 이외의 모든 경로는 인증해야 됨
				.authenticated();
		
			
			// filter 등록
			// 매 요청마다
			// CorsFilter 실행한 후에
			// jwtAuthenticationFilter 실행한다.
			http.addFilterAfter(
					jwtAuthenticationFilter, 
					CorsFilter.class);
			
			return http.build();
	};
  
  ```
  
  > 📌 spring security 5.7이상에서 더 이상 `WebSecurityConfigurerAdapter` 어댑터를 지원하지 않게 되었음. 
  
  따라서 `SecurityFilterChain` 을 Bean으로 등록하면서 시큐리티 설정을 해주어야함.
  
  <br>
  
## 9. 인증 프론트엔드 통합
### 서버-사이드 라우팅
![image](https://user-images.githubusercontent.com/87354210/206144642-b7e384fc-9fb3-46f5-ad7e-9871d3d6ed12.png)
- 서버는 login 경로를 보고 login.html 페이지를 반환 -> 브라우저는 `login.html` 을 렌더링 -> 웹 페이지 새로고침

<br>

### 클라이언트-사이드 라우팅
![image](https://user-images.githubusercontent.com/87354210/206145561-9dc1b51b-e9b1-4efe-9ea4-64c6ba555f82.png)
- `http://localhost:3000` 에 접속하면 프론트엔드 서버가 리액트 애플리케이션 리턴
- 이 애플리케이션이 필요한 모든 리소스를 갖고 있음.
- 모든 것은 클라이언트 사이드, 즉 브라우저 내부에서 실행되고 서버로는 아무것도 요청하지 않음.
- 라이브러리 필요 : 우리 프로젝트에서는 `react-router-dom` 사용


  
