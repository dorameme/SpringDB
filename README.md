# SpringDB 공부하면서 배운 것들 

## 커리큘럼

| 날짜   | 정리 | 커리큘럼 내용                                         |
|------|----|-------------------------------------------------|
| 8/30 | ☑️ | [jdbc 이해](#jdbc-이해)                             |
| 8/31 |  ☑️ | [커넥션풀과 데이터소스 이해](#커넥션풀과-데이터소스-이해)               |
| 9/5  |  ☑️  | [트랜잭션 이해](#트랜잭션-이해)                             |
| 9/6  |  ☑️  | [스프링과 문제 해결 - 트랜잭션](#스프링과-문제-해결--트랜젝션)          |
| 9/6  |  ☑️  | [자바 예외 이해](#자바-예외-이해)                           |
| 9/9  |  ☑️  | [스프링과 문제 해결 - 예외 처리, 반복](#스프링과-문제-해결--예외 처리,반복) |




## jdbc 이해

### 1. spring project 만들기

Spring Boot 애플리케이션은 내장된 서블릿 컨테이너(Tomcat, Jetty, Undertow 등)를 사용하여 실행한다.      
이 내장된 서블릿 컨테이너는 Spring Boot 애플리케이션을 실행하고 웹 요청을 처리한다.
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

### 8. PreparedStatement
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

### 1. 커넥션 풀 

db커넥션을 만들려면...   
요청을 받고 Tcp/ip 커넥션 연결 뒤  DB에서 id,pw 인증을 통해 내부 db세션을 생성한뒤   
db에서 커넥션 생성했다고 응다보내면 db드라이버는 커넥션 객체를 생성해 클라이언트에 반환함.   

(클라이언트) 요청-> 커넥션조회-> TCP/IP 커넥션 연결 -> ID,PW  부가정보전달 -> 커넥션 생성-> 커넥션 반환    
이 모든 과정이필요하고 SQL실행하는 시간도 필요하니 응답속도가 느릴 수밖에 없다.

따라서 커녁션을 미리 만들어 커넥션 풀에 커넥션을 관리하는 방법이 있다. (커넥션 수는 서비스의 특징과 서버 스펙에 따라 다르지만 기본값은 보통 10개)
커넥션 풀에 있는 커넥션은 이미 db와 TCP/IP 로 연결되어있으므로 언제든 SQL을 DB에 전달가능
요즘은 hikariCP라는 커넥션풀 오픈소스를 많이 씀.


### 2. DataSource! 커넥션을 획득하는 방법을 추상화
여러 커넥션풀들을 호환성있게 사용하기 위해 자바는 DataSource 라는 인터페이스를 제공한다.    
DataSource 는 커넥션을 획득하는 방법을 추상화 하는 인터페이스이다.    
이 인터페이스의 핵심 기능은 커넥션 조회 하나이다.     
개발자는 커넥션 풀에 따른 코드가 아닌 DataSource 인터페이스에만 의존하도록 애플리케이션 로직을 작성하면 된다. 

### 3. 설정과 사용의 분리
DriverManager 는 커넥션을 획득할 때 마다  파라미터를 계속 전달.   
반면에 DataSource 를 사용하는 방식은 처음 객체를 생성할 때만 필요한 파리미터를 넘겨두고    
단순히 dataSource.getConnection() 만 호출하면 됨.
따라서 향후 변경에 유연히 대처가능!

### 4. 자바 직렬화
자바 직렬화(Java Serialization)는
자바 객체를 직렬화하여 이를 이진 데이터로 변환하는 프로세스를 가리킴          
직렬화된 데이터는 파일로 저장하거나 네트워크를 통해 전송하고, 
이후에 역직렬화를 통해 다시 객체로 복원할 수 있다.         
이러한 프로세스는 객체의 상태를 보존하고 공유하기 위해 사용


## 트랜잭션 이해

### 1. 트랜잭션 - 개념 이해
데이터베이스를 이용하는 대표적인 이유 -> 트랜젝션을 지원하기 때문
모든 작업이 성공해서 데이터베이스에 정상 반영하는 것을 커밋( Commit )이라 하고,   
작업 중 하나라도 실패 해서 거래 이전으로 되돌리는 것을 롤백( Rollback )이라 한다       

트랜잭션 ACID

원자성 (Atomicity): 원자적으로 간주되어 모든작업은 성공 또는 실패해야함.    
일관성 (Consistency): 데이터베이스는 규칙 제약조건 준수하며 일관된 상태 유지.   
고립성 (Isolation):  여러 트랜잭션이 동시 실해되어도 다른 트랜잭션의작업에 영향을 주지 않아야 함     
지속성 (Durability):안정적으로 저장되어 데이터보존      

### 2. 체크예외 vs 언체크예외
에러(Error)-> 처리불가능
예외(Exception) -> 어플리케이션 코드예외
1.체크 예외(Check Exception)
2.언체크 예외(Uncheck Exception)

체크 예외 (Checked Exception):

체크 예외는 컴파일러가 강제로 예외 처리 코드를 작성하도록 요구하는 예외다.    
즉, 예외를 발생시킬 수 있는 메소드에서 throws 문을 사용하여 해당 예외를 선언해야 한다.    
대표적인 체크 예외로는 IOException, SQLException 등이 있다.    
예외 처리를 하지 않거나 예외를 던지지 않으면 컴파일 오류가 발생.   

언체크 예외 (Unchecked Exception):

언체크 예외는 예외 처리를 강제로 요구하지 않는다.    
따라서 메소드에서 언체크 예외를 던져도 throws 문을 사용하여 선언하지 않아도 된다.     
대표적인 언체크 예외로는 NullPointerException,
ArrayIndexOutOfBoundsException, IllegalArgumentException, 
RuntimeException 및 그 하위 예외들이 있다.

## 스프링과 문제 해결 - 트랜잭션

### 1. 트랜잭션
스프링이 제공하는 트랜잭션 매니져는 두가지 역할이 있다
1. 트랜잭션 추상화
2. 리소스 동기화(맞추어 사용)

트랜잭션 템플릿까지 사용하면 트램잭션의 반복되는 코드 제거 가능

## 자바 예외 이해

### 예외 계층
<p align="center">
  <img src="https://github.com/dorameme/steady_algorithm_learner/assets/68580993/fca8fb12-0408-45f1-80cb-e2bc2a1d17d0">
</p>
Throwable 최상위 예외-> Error / Exception이 하위에러

Error 는 잡으면 안되니 Exception부터 잡아준다.

예외처리를 하지 않으면 호출한 곳으로 예외를 계속 던지게 된다.   
예외를 처리하지않고 계속 던지면 main() 스레드의 경우 예외로그를 출력하며 시스템 종료.      


### 체크예외의 활용
기본적으로 언체크(런타임)예외를 사용한다.    
체크예외는 비즈니스 로지상 의도적으로 던지는 예외에만 사용.  
해당 에외를 반드시 잡아서 처리해야하는 경우 체크예외 사용!  
예를들어        
1. 계좌 이체 실패 예외
2. 결제시 포인트 부족예외
3. 로그인 ID,PW불일치 예외

체크 예외는 
1. 복구불가능한 예외 와..
2. 의존 관계에 대한 문제가 있다.

### 언체크예외의 활용
체크예외를 언체크로 바꾸면...

런타임 예외 - 대부분 복구 불가능한 예외      
시스템에서 발생한 예외는 대부분 복구 불가능 예외이다.    
런타임 예외를 사용하면 서비스나 컨트롤러가 이 런 복구 불가능한 예외를 신경쓰지 않아도 된다. 

런타임 예외 - 의존 관계에 대한 문제
런타임 예외는 해당 객체가 처리할 수 없는 예외는 무시하면 된다.     
따라서 체크 예외 처럼 예외를 강제로 의존하지 않아도 된다.
*에외를 전환할떄는 꼭 기존예외를 포함하여야 한다.


