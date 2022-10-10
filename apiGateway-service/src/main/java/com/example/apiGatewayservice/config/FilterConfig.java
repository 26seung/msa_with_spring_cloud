package com.example.apiGatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder){
        return builder.routes()
                .route(r -> r.path("/first-service/**") //  path 가 들어오면 uri 로 이동한다.
                        .filters(f -> f.addRequestHeader("first-request","first-request-header")
                                .addResponseHeader("first-response","first-response-header"))
                        .uri("http://localhost:8081"))
                .route(r -> r.path("/second-service/**") //  path 가 들어오면 uri 로 이동한다.
                        .filters(f -> f.addRequestHeader("second-request","second-request-header")
                                .addResponseHeader("second-response","second-response-header"))
                        .uri("http://localhost:8082"))
                .build();
    }
}
