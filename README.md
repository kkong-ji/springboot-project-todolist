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
