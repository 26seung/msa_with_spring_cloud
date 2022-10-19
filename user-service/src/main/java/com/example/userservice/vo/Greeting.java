package com.example.userservice.vo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
// 빈으로 등록하기 위해서 @Component 사용
@Component
public class Greeting {

    @Value("${greeting.message2}")
    private String message;
}
