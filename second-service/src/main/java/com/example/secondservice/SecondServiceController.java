package com.example.firstservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//  gateway 등록한 uri 와 predicates 의 설정으로
//  http:localhost:8000/first(second)-service/**
//  형식으로 매핑이 진행 됨

@RestController
@RequestMapping("/first-service")
public class FirstServiceController {

    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome to the First service";
    }
}