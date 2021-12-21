package com.cocodan.triplan.aop.aspect;

import com.cocodan.triplan.jwt.JwtAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Around("execution(* com.cocodan.triplan..*Controller.*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {

        Object userId = getUserId();
        String request = joinPoint.getSignature().toShortString();
        long startTime = System.currentTimeMillis();
        log.info("[{}] request : {} start", userId, request);

        try {
            Object result = joinPoint.proceed();
            long resultTime = getResultTime(startTime);
            log.info("[{}] request : {} end time = {}(ms)", userId, request, resultTime);
            return result;
        } catch (Exception e) {
            long resultTime = getResultTime(startTime);
            log.warn("[{}] request : {} exception {}(ms)", userId, request, resultTime);
            throw e;
        }
    }

    private Object getUserId() {
        Object userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userId instanceof JwtAuthentication) {
            userId = ((JwtAuthentication) userId).getId();
        }
        return userId;
    }

    private long getResultTime(long startTime) {
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
}
