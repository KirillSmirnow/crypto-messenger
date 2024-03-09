package cryptomessenger.desktop.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class ServiceLogger {

    @Around("@within(org.springframework.stereotype.Service)")
    @SneakyThrows
    public Object logInvocation(ProceedingJoinPoint joinPoint) {
        var signature = getSignature(joinPoint);
        log.info("{}", signature);
        try {
            var returnValue = joinPoint.proceed();
            log.info("{} -> {}", signature, returnValue);
            return returnValue;
        } catch (Exception exception) {
            log.info("{} !> {}", signature, exception.getMessage());
            throw exception;
        }
    }

    private String getSignature(JoinPoint joinPoint) {
        var shortSignature = joinPoint.getSignature().toShortString();
        var arguments = Arrays.toString(joinPoint.getArgs());
        return shortSignature.replace("..", arguments);
    }
}
