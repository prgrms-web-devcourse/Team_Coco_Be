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

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        //인메모리에 username, password, role 설정
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("pwd")    // 팀에서 정한 비밀번호를 설정해 주세요.
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }

}