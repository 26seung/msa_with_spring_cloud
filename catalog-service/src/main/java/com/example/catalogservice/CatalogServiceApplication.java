package com.example.catalogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
// 사용하지 않아도 인스턴스가 등록됩니다.
// DiscoveryClient 인터페이스를 구현한 클래스를 검색해서 Service Registry에 등록하게 되는데,
// 기본적으로 Spring Boot 서버를 자동으로 등록하도록 설정되어 있습니다.
// 그러나, 이 설정은 autoRegister=false 에 의해서 변경될수도 있기 때문에, 작업해 주시는게 좋다고 생각됩
public class CatalogServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatalogServiceApplication.class, args);
	}

}
