package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.Greeting;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
//@RequestMapping("/user-service/")
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
        return String.format("It's Working in User Service on PORT %s",env.getProperty("local.server.port"));
    }
    @GetMapping("/welcome")
    public String welcome(){
        return env.getProperty("greeting.message");
    }
    @GetMapping("/welcome2")
    public String welcome2(){
        return greeting.getMessage();
    }


    // 회원가입
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody RequestUser user){

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(user, UserDto.class);

        userService.createUser(userDto);

        ResponseUser responseUser = mapper.map(userDto,ResponseUser.class);

        System.out.println("user ? " + user);
        System.out.println("userDto ? " + userDto);
        System.out.println("responseUser ? " + responseUser);
        // POST 이용 시 Status 201 번 성공 메시지가 더 정확하다
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUser(){
        Iterable<UserEntity> userList = userService.getUserByAll();

        List<ResponseUser> result = new ArrayList<>();
        userList.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseUser.class));
        });
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId){
        UserDto userList = userService.getUserByUserId(userId);

        ResponseUser returnValue = new ModelMapper().map(userList, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }

}
