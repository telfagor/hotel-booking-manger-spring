package com.bolun.hotel.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class ServiceLoggingAspect {

    @Pointcut("within(com.bolun.hotel.service.*Service)")
    public void isServiceLayer() {

    }

    @Around("isServiceLayer() && execution(* com.bolun.hotel.service..*Service.*(..))")
    public Object logAroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        log.info("Class: {} Entering method: {} with arguments: {}", className, methodName, Arrays.toString(args));

        Object result;
        try {
            result = joinPoint.proceed();
            log.info("Class: {} Exiting method: {} with result: {}", className, methodName, result);
        } catch (Exception e) {
            log.error("Class: {} Exception in method: {} with arguments: {}", className, methodName, Arrays.toString(args), e);
            throw e;
        }

        return result;
    }
}
