package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.Greeting;
import com.example.userservice.vo.RequestUser;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    UserService userService;



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


    //
    @PostMapping("/users")
    public String createUser(@RequestBody RequestUser user){

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(user, UserDto.class);

        userService.createUser(userDto);

        return "Create User method is called";
    }
}
