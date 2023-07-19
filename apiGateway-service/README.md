SpringBoot 2.4 이전 버전에서는 `Zuul` 을 이용한 Gateway 방식을 사용 하였으나,  
2.4 이상 버전 에서는 `cloud gateway` 의존성을 사용한다.

`zuul` 은 `Tomcat (동기 방식)` 로 구동 / 
`cloud gateway` 은 `Netty (비동기 방식)` 로 구동

---
#### yml 설정파일

```yaml
server:
  port: 8000

spring:
  application:
    name: apiGateway-service
  cloud:
    gateway:
      routes:
        - id: first-service
          uri: http://127.0.0.1:8081/     # 포워딩 정보
          predicates:                     # 조건절: Path 정보에 따라 uri 설정으로 포워딩
            - Path=/first-service/**
        - id: second-service
          uri: http://127.0.0.1:8082/
          predicates:
            - Path=/second-service/**

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
    service-url:
      defaultZone: http://127.0.0.1:8761/eureka
```

`cloud.gateway.routes:` 를 등록하여 각 서비스를 등록한다.  
`http://localhost:8081(8082)/first(second)-service/welcome` 형식으로 접속 가능하던 주소가,  
`http:localhost:8000/first(second)-service/**` 형식으로 매핑된다.

---

yml 설정이 아닌 필터를 이용한 방법도 사용이 가능하다.

`RouteLocator` 를 `@Bean` 으로 받아 사용하면 된다.
![image](https://user-images.githubusercontent.com/79305451/194903355-13db58c1-60d4-4379-af0b-a9d175da9c53.png)

`Path ("first-service")` 가 들어오면 `uri ("http://localhost:8081")` 로 이동한다.
해당 방식으로 `Header` 값을 요청하고 받아오는 것이 가능하다.

---

다시 yml 파일에 설정하는 방식은 다음과 같이 `filters:` 를 추가하여 사용한다.

```yaml
  cloud:
    gateway:
      routes:
        - id: first-service
          uri: http://localhost:8081/
          predicates:
            - Path=/first-service/**
          filters:
            - AddRequestHeader=first-request, first-request-header2
            - AddResponseHeader=first-response, first-response-header2    # first-response 헤더에 first-response-header2 값이 담김
```
---

#### CustomFilter 사용

`AbstractGatewayFilterFactory`를 상속 받아서 `apply` 메서드를 사용하여 적용한다.
```yaml
  cloud:
    gateway:
      routes:
        - id: first-service
          uri: http://localhost:8081/
          predicates:
            - Path=/first-service/**
          filters:
             - name: CustomFilter   # 적용할 route 별로 CustomFilter 설정 등록
```

#### GlobalFilter 사용

GlobalFilter 는 가장 먼저 시작되고 가장 마지막에 수행된다. (GlobalFilter - CustomFilter - GlobalFilter 순서)

```yaml
spring:
  application:
    name: apiGateway-service
  cloud:
    gateway:
      default-filters:
        - name: GlobalFilter    # CustomFilter 와 달리 모든 route 에 공통적으로 설정이 가능
          args:
            baseMessage: Spring Cloud Gateway Global Filter..
            preLogger: true
            postLogger: true
```

#### LoggingFilter 사용

`LoggingFilter` 는 `GlobalFilter` 와 `CustomFilter` 중간에 실행되어 짐  
`apply` 메서드 내에서 `Ordered.HIGHEST_PRECEDENCE` 사용으로 우선순위를 설정해 줄 수 있다.


----

#### Load Balancer 사용

- 클라이언트 요청에 대한 흐름
  - 사용자가 요청정보를 `1)API Gateway` 로 보내면 `2)Eureka 서버`로 내용을 전달하고 위치정보를 전달받아 `3)API Gateway` 에서 해당 요청에 대한 `4)인스턴스로 포워딩`을 시켜준다.



`Eureka` 의 사용용도는 `Service Discovery / Registry` 의 역활을 하게 된다.  
모든 서비스에 대한 `IP정보`를 기억하고 있지 않아도 된다. yml 설정 `routes:uri` 에 `serviceName`으로 호출 가능 `예시 -> uri: lb://MY-FIRST-SERVICE`

yml 파일에 Eureka 설정 등록을 진행 필요
```yaml
eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://127.0.0.1:8761/eureka

spring:
   application:
      name: apiGateway-service
   cloud:
      gateway:
         routes:
            - id: first-service
              uri: lb://MY-FIRST-SERVICE    # "lb://서비스이름" 형식으로 uri 설정이 변경
              predicates:
                 - Path=/first-service/**
```


새로운 포트의 사용을 위해서 `Edit Configurations` 설정의 `VM옵션` 에 `-Dserver.port=9092` 입력하여 새로운 포트를 사용하여 실행할 수 있다. 

<img width="1338" alt="image" src="https://user-images.githubusercontent.com/79305451/195637888-2592c022-f394-451c-851f-4da6123c6fc0.png">

이렇게 수행이 되고 로드밸런서에 의해 분산처리 되는 진행이 가능하다.

---

랜덤포트를 사용하여 고정적인 포트번호가 아닌 유동적인 포트번호를 사용하여 여러 파일을 실행시킬 수 있다. 

 랜덤포트를 사용하기 위해서는
- `port: 0` 으로 설정  / 대시보드에는 0 인 포트 목록 하나로 보여지게 된다. 
- 구분을 위해서는   `instance: instance-id` 입력을 해주어야 한다.
  - 랜덤포트를 사용하여도 `port: 0` 으로 적용되어 하나만 대시보드에 보여짐
```yaml
  instance:   # 인스턴스ID를 통해 포트구분
    instance-id: ${spring.application.name}:${spring.application.instance_id:${random.value}}
```

랜덤포트 사용 시 포트번호 확인하는법
1. `Environment env;` 생성자 사용하여 `env.getProperty("local.server.port")` 받아올 수 있음
2.     public String check(HttpServletRequest request){
        log.info("Server port={}",request.getServerPort());
        return null;}
---


