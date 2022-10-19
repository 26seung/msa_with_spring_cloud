eureka.client.fetch-registry= true 는 EUREKA 서버로부터 인스턴스들의 정보를 주기적으로 가져올 것인지 설정하는 속성입니다. true로 설정하면, 갱신 된 정보를 받겠다는 설정입니다.

```
server:
    port: 0
```
 
으로 설정 시 랜덤포트를 사용하여 포트의 중첩없이 자동으로 포트를 할당받아 사용이가능하다.  
하지만 Eureka 페이지에서 확인시에는 0번포트로 하나만확인이 가능하며 이를 분류해서 보기 위해서는

```
  instance:
    instance-id: ${spring.cloud.client.hostname}:${spring.application.instance_id:${random.value}}
```
를 yml 에 추가하면 구분하여 확인이 가능하다