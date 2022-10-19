package com.example.userservice.controller;

import com.example.userservice.vo.Greeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UserController {

    //  yml 파일의 내용을 가져오기 위해서는
    //  1번 방법 : Environment 의 env.getProperty("greeting.message") 사용
    //  2번 방법 : @Value("${greeting.message}") 사용
    private Environment env;
    public UserController(Environment env) {
        this.env = env;
    }
    @Autowired
    private Greeting greeting;



    @GetMapping("/health_check")
    public String status(){
        return "It's Working in User Service";
    }
    @GetMapping("/welcome")
    public String welcome(){
        return env.getProperty("greeting.message");
    }
    @GetMapping("/welcome2")
    public String welcome2(){
        return greeting.getMessage();
    }
}
