package com.rdrcelic.troskovi.expenses.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Slf4j
@Aspect
@Configuration
public class ControllerLoggingAspect {

    private final ObjectMapper objectMapper;

    public ControllerLoggingAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerPointcut() {}

    @Pointcut("execution(public * *(..))")
    public void endPointsPointcut() {}

    @Pointcut("endPointsPointcut() && restControllerPointcut()")
    public void restEndPointsPointcut() {}

    @Before("restEndPointsPointcut()")
    public void logBefore(JoinPoint joinPoint) {
        // Advice
        log.info("Entering method: {}", joinPoint.getSignature().getName());
        log.debug("Arguments: {}", Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "restEndPointsPointcut()", returning = "result")
    public void logAfterReturn(JoinPoint joinPoint, Object result) {
        log.info("Exiting method: {}", joinPoint.getSignature().getName());
        if (log.isDebugEnabled()) {
            try {
                log.debug("Response: {}", objectMapper.writeValueAsString(result));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    @AfterThrowing(pointcut = "restEndPointsPointcut()", throwing = "exception")
    public void logAfterExceptionThrown(JoinPoint joinPoint, Object exception) {
        log.error("Exception {} occured in method: {}", exception.getClass().getCanonicalName(), joinPoint.getSignature().getName());
    }
}
