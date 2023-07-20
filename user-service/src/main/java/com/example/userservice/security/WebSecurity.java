package com.example.userservice.security;

import com.example.userservice.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@Configuration      //  ㄷㅏ른 Bean 보다 우선순위 등록
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private UserService userService;
    private BCryptPasswordEncoder encoder;
    private Environment env;

    public WebSecurity(UserService userService, BCryptPasswordEncoder encoder, Environment env) {
        this.userService = userService;
        this.encoder = encoder;
        this.env = env;
    }

    //  권한 configure
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
//        http.authorizeRequests().antMatchers("/users/**").permitAll();
        http.authorizeRequests()
                .antMatchers("/error/**").permitAll()
                .antMatchers("/**")
                .hasIpAddress("192.168.0.18")
                .and()
                .addFilter(getAuthenticationFilter());

        http.headers().frameOptions().disable();        //      h2 데이터베이스 접속위해서 설정 (프레임으로 이루어짐)
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
        authenticationFilter.setAuthenticationManager(authenticationManager());

        return authenticationFilter;
    }

    //  인증 configure
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(encoder);
    }
}
