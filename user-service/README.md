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

---

유레카 서비스를 가동하기 위해서 앞서 만들어 놓은 `discoveryservice` 프로젝트를 수행해 주도록 한다

인텔리제이는 많은 리소스를 잡아먹기 때문에 새창을 열어서 프로젝트를 가동하는 것은 부담이 될 수 있다.
그래서 `터미널` 환경에서 프로젝트를 가동면 부담을 줄일 수 있다.

gradle 를 설치 하지 않아도 다음과 같이 인텔리제이 안의 내장 gradle 을 사용하여 명령어를 입력할 수 있다.
```
jar 파일 빌드 명령어
./gradlew build 

그냥 프로젝트 실행 명령어
./gradlew bootrun
```

---

yml 파일의 내용을 가져오기 위해서 다음과 같은 방법을 이용한다.
- 1번 방법 : `Environment` 의 `env.getProperty("greeting.message")` 사용
- 2번 방법 : `@Value("${greeting.message}")` 사용

하는 방법들이 존재 한다.

---

h2 데이터 베이스 사용 시 JDBC 드라이버의 의존성도 같이 사용해 주어야 생성이 진행된다.

---

