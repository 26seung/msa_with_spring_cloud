SpringBoot 2.4 이전 버전에서는 `Zuul` 을 이용한 Gateway 방식을 사용 하였으나,  
2.4 이상 버전 에서는 `cloud gateway` 의존성을 사용한다.

`zuul` 은 `Tomcat` 로 구동 / 
`cloud gateway` 은 `Netty` 로 구동

---
#### yml 설정파일
```
server:
  port: 8000

spring:
  application:
    name: apiGateway-service
  cloud:
    gateway:
      routes:
        - id: first-service
          uri: http://127.0.0.1:8081/
          predicates:
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

yml 설정이 아닌 방법도 사용이 가능하다.

`RouteLocator` 를 `@Bean` 으로 받아 사용하면 된다.
![image](https://user-images.githubusercontent.com/79305451/194903355-13db58c1-60d4-4379-af0b-a9d175da9c53.png)

`Path ("first-service")` 가 들어오면 `uri ("http://localhost:8081")` 로 이동한다.
해당 방식으로 `Header` 값을 요청하고 받아오는 것이 가능하다.

---

다시 yml 파일에 설정하는 방식은 다음과 같이 `filters:` 를 추가하여 사용한다.

```
  cloud:
    gateway:
      routes:
        - id: first-service
          uri: http://localhost:8081/
          predicates:
            - Path=/first-service/**
          filters:
            - AddRequestHeader=first-request, first-request-header2
            - AddResponseHeader=first-response, first-response-header2
```
---

#### CustomFilter 사용

`AbstractGatewayFilterFactory`를 상속 받아서 `apply` 메서드를 사용하여 적용한다.

#### GlobalFilter 사용

GlobalFilter 는 가장 먼저 시작되고 가장 마지막에 수행된다.

#### LoggingFilter 사용

`LoggingFilter` 는 `GlobalFilter` 와 `CustomFilter` 중간에 실행되어 짐  
`apply` 메서드 내에서 `Ordered.HIGHEST_PRECEDENCE` 사용으로 우선순위를 설정해 줄 수 있다.