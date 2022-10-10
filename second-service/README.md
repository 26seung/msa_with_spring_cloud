
---

yml 설정
```
server:
  port: 8082

spring:
  application:
    name: my-second-service

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false

```

`gateway` 등록한 `uri` 와 `predicates` 의 설정으로  
`http:localhost:8000/first(second)-service/**`  형식으로 매핑이 진행 됨

---
