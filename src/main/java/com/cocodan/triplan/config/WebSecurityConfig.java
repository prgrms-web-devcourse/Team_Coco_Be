package com.cocodan.triplan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity  //@EnableWebSecurity : Spring Security의 웹 보안 지원을 활성화하고 Spring MVC 통합을 제공
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //configure(HttpSecurity http) : 보안 처리할 경로와 처리하지 않을 경로 정의
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and();
    }

    @Bean //<-- 이 부분을 빼먹어서 '자격 증명에 실패하였습니다' 메세지를 찾는다고 고생. 주의!!!
    @Override
    public UserDetailsService userDetailsService() {
        //인메모리에 username, password, role 설정
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("pwd")
                        .roles("USER")
                        .build();

        System.out.println("password : " + user.getPassword());

        return new InMemoryUserDetailsManager(user);
    }

}