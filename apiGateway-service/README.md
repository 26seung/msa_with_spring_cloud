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
