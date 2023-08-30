# SpringDB 공부하면서 배운 것들 

## 커리큘럼

| 날짜      | 정리 | 커리큘럼 내용                                         |
|---------|----|-------------------------------------------------|
| 8/30    | ☑️ | [jdbc 이해](#jdbc-이해)                             |
|         | ️  | [커넥션풀과 데이터소스 이해](#커넥션풀과-데이터소스-이해)               |
|         |    | [트랜잭션 이해](#트랜잭션-이해)                             |
|         |    | [스프링과 문제 해결 - 트랜잭션](#스프링과-문제-해결--트랜젝션)          |
|         |    | [자바 예외 이해](#자바-예외-이해)                           |
|         |    | [스프링과 문제 해결 - 예외 처리, 반복](#스프링과-문제-해결--예외 처리,반복) |




## jdbc 이해

### 1. spring project 만들기

Spring Boot 애플리케이션은 내장된 서블릿 컨테이너(Tomcat, Jetty, Undertow 등)를 사용하여 실행한다.      
이 내장된 서블릿 컨테이너는 Spring Boot 애플리케이션을 실행하고 웹 요청을 처리합니다
Spring Boot 애플리케이션이 서블릿 컨테이너의 생명주기에 의존하기 때문에    
만일 (톰캣도 네티도 없이) 서블릿컨테이너에 아무것도 없으면 스프링부트도 종료된다.    
즉 *WAS가 항시 대기상태로 머물러있어야 스프링부트도 계속 실행되는 상태가 된다.   
따라서 시작 중 예기치 않은 종료를 해결하려면 적절한 스타터를 사용하여 완전히 구성된 인스턴스를 가져와야 한다.    
현재 Spring Boot에는 내장된 Tomcat, Jetty 및 Undertow 서버에 대한 지원이 포함되어 있다.
spring-boot-starter-web을 추가하면 스프링 부트 프로젝트에 필요한 톰캣 서버를 포함한 대부분의 라이브러리가 추가된다.    
*WAS는 간단히 Web Server + Web Container(Sevlet Container)

### 2. H2 database

H2 데이터베이스의 URL은 데이터베이스 연결 및 생성 위치를 지정하는 데 사용.(원하는대로 설정가능)
같은 URL을 사용하여 해당 데이터베이스에 연결할 수 있다.

1. 설정이 단순
2. 프로그램이 가볍다. 대규모에서 쓰기엔안정성과 성능은 떨어짐
3. 인메모리 DB
   (disk-based DB 와 달리 외부장치가아닌 RAM에 데이터저장)-> 따라서 CRUD 연산 속도가 빠르다
4. 애플리케이션 종료시 데이터 삭제


### 3. JDBC 이해 
JDBC(java database connectivity)  
JDBC는 데이터베이스에서 자료를 쿼리하거나 업데이트하는 방법을 제공   
자바에서 데이터베이스 접속을 위한 자바API.(Application Programming Interface)

1. Tcp/ip로 커넥션연결
2. SQL전달
3. 결과응답

위 과정이 db마다 달랐다.
-> 데이터베이스 종류가 다양해져서 호환성을 위해 JDBC라는 자바표준이 생김


위 세과정을 다해줌
jdbc표준 인터페이스에 따라 구현된 드라이버를 사용하여 데이터베이스와 연결 제공
Jdbc등장으로 데이터베이스 변경이 쉬워짐

Ex)애플리케이션 로직 > jdbc표준 인터페이스 -> oracle 드라이버 -> 오라클 DB

결론 -> 덕분에 개발자는 JDBC만 쓸 줄 알면 된다.

### 4. SQL Mapper & ORM

JDBC를 직접사용하는것보다 편한 방법이 많이나옴.

애플리케이션 로직-> JDBC    
애플리케이션 로직-> SQP Mapper->JDBC

SQP Mapper= ex) JdbcTemplate, MyBatis

1.응답결과를 객체로 변환해줘서 편함   
2.JDBC 반복코드제거

하지만 개발자가 직접 sql작성해야함

따라서 ORM기술이 나옴   
객체를 관계형데이터베이스 테이블과 매핑해주는 기술 -> SQL을 대신 만들어준다.    
ex>JPA 인터페이스의 하이버네이트 구현체를 많이 쓴다    
애플리케이션 로직->JPA->하이버네이트(JPA구현체)->JDBC

SQL Mapper - SQL만 배우면 쉽게사용     
ORM - SQL도 안작성해도되어 개발생산성은 매우 높지만 깊이있는 학습필요

### 5. 데이터베이스 연결 
Connection connection= DBConnectionUtil.getConnection();

설명:DriverManager.getConnection()로 받은 객체를
Jdbc connection 인터페이스를 구현한  Org.h2.jdbc.jdbcConnection 구현체(H2 드라이버)에 저장   

애플리케이션 로직->드라이버메니저-> h2 드라이버 호출

### 6. SQL 인젝션이란?
입력이 가능한 폼에 공격자가 악의적인 SQL문을 삽입하여 디비 정보 열람 및 정보를 조작하는 해킹
이를 막기위해 PreparedStatement를 이용해 데이터를 ?로 바인딩하여 넣어줘야한다.
바인딩된 변수는 쿼리와 별도로 서버로 전송되므로 사용자가 입력한 구문이 쿼리로 실행될 수가 없다.

### 7. Lombok의 어노테이션
자바 언어를 더 간결하고 보다 효율적으로 작성할 수 있도록 도와주는 라이브러리

@Getter, @Setter - 자동으로 Getter와 Setter 메서드를 생성  
@NoArgsConstructor - 파라미터 없는 기본 생성자를 자동으로 생성  
@AllArgsConstructor - 모든 필드를 파라미터로 받는 생성자를 자동으로 생성  
@ToString - toString() 메서드는 객체의 필드 값을 문자열로 반환  
@EqualsAndHashCode - equals()와 hashCode() 메서드를 자동으로 생성     
롬복의 @Data는 @Getter, @Setter, @RequiredArgsConstructor, @ToString, @EqualsAndHashCode를 한꺼번에 적용한 것과 같은 효과를 가짐.
주로 데이터 객체 (DTO)에서 사용됩니다.

### PreparedStatement
PreparedStatement은 SQL문을 저장,실행하는 객체
주요 특징
- 인자값으로 전달이 가능하다.(동적 쿼리)
- 가독성이 높다.
- 기존 Statement 보다 성능이 좋다.

Execute
1. 수행결과로 Boolean 타입의 값을 반환
2. 모든 구문을 수행할 수 있다

ExecuteQuery
1. 수행결과로 ResultSet 객체인 rs변수에 객체의 값을 저장
2. SELECT 구문을 수행할 때 사용되는 함수

ExecuteUpdate
1. 수행결과로 Int 타입의 값을 반환    
2. SELECT 구문을 제외한 다른 구문을 수행할 때 사용되는 함수     
   INSERT / DELETE / UPDATE 관련 구문에서는 반영된 레코드의 건수를 반환    
3. CREATE / DROP 관련 구문에서는 -1 을 반환.

애초에 Select하는 경우에만 객체값이 필요하기에 각각 메소드마다 반환 값이 다름.

## 커넥션풀과 데이터소스 이해