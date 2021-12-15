package com.cocodan.triplan.aop.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* com.cocodan.triplan.member..*.*(..))")
    public void allMember(){}

    @Pointcut("execution(* com.cocodan.triplan.post..*.*(..))")
    public void allPost(){}

    @Pointcut("execution(* com.cocodan.triplan.schedule..*.*(..))")
    public void allSchedule(){}

    @Pointcut("allMember() || allPost() || allSchedule()")
    public void allDomain(){}
}
