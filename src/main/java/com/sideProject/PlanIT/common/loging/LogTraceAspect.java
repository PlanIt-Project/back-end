package com.sideProject.PlanIT.common.loging;

import com.sideProject.PlanIT.common.loging.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class LogTraceAspect {

    private final LogTrace logTrace;

    public LogTraceAspect(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    @Pointcut("execution(* com.sideProject.PlanIT.domain.*.controller.*Controller.*(..))")
    public void controllerAdvice() {}

    @Pointcut("execution(* com.sideProject.PlanIT.domain.*.service.*Service.*(..))")
    public void serviceAdvice() {}

    @Around("controllerAdvice()||serviceAdvice()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceStatus status = null;

        try {
            String message = joinPoint.getSignature().toShortString();
            status = logTrace.begin(message);

            //로직 호출
            Object result = joinPoint.proceed();

            logTrace.end(status);
            return result;
        } catch (Exception e){
            logTrace.exception(status,e);
            throw e;
        }
    }
}
