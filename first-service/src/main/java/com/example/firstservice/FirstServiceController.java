package com.example.firstservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


//  gateway 등록한 uri 와 predicates 의 설정으로
//  http:localhost:8000/first(second)-service/**
//  형식으로 매핑이 진행 됨

@RestController
@RequestMapping("/first-service")
@Slf4j
public class FirstServiceController {

    Environment env;

    public FirstServiceController(Environment env){
        this.env = env;
    }

    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome to the First service";
    }

    @GetMapping("/message")
    public String message(@RequestHeader("first-request") String header){
        log.info(header);
        return "Hello World in First Service";
    }

    @GetMapping("/check")
    public String check(HttpServletRequest request){
        log.info("Server port={}",request.getServerPort());

        return String.format("Hi, there this is a message from First Service on PORT %s", env.getProperty("local.server.port"));
    }
}
