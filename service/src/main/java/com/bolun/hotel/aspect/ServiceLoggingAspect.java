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

    /**
     * Почему-то этот pointcut не работает с @Around
     */
    @Pointcut("@within(com.bolun.hotel.service.*Service)")
    public void isServiceLayer() {

    }

    @Around("execution(* com.bolun.hotel.service..*Service.*(..))")
    public Object logAroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        log.info("Entering method: {} with arguments: {}", methodName, Arrays.toString(args));

        Object result;
        try {
            result = joinPoint.proceed();
            log.info("Exiting method: {} with result: {}", methodName, result);
        } catch (Exception e) {
            log.error("Exception in method: {} with arguments: {}", methodName, Arrays.toString(args), e);
            throw e;
        }

        return result;
    }
}
