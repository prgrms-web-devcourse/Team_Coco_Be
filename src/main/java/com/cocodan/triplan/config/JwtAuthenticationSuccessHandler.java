package com.cocodan.triplan.config;

import com.cocodan.triplan.jwt.JwtAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        JwtAuthentication jwtAuthentication = (JwtAuthentication) authentication.getPrincipal();

        response.setHeader("token", jwtAuthentication.getToken());
    }
}
