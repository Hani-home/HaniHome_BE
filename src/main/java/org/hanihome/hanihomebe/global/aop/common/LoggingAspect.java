package org.hanihome.hanihomebe.global.aop.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /** 서비스 진입·종료 로그 */
    @Around("org.hanihome.hanihomebe.global.aop.common.CommonPointCuts.inServiceLayer()")
    public Object logService(ProceedingJoinPoint pjp) throws Throwable {
        String signature = pjp.getSignature().toShortString();
        Object[] args = pjp.getArgs();

        log.info("[Service START] {} args={}", signature, args);
        try {
            Object result = pjp.proceed();
            log.info("[Service  END] {} result={}", signature, result);
            return result;
        } catch (Throwable ex) {
            log.error("[Service ERROR] {} ex={}", signature, ex.toString());
            throw ex;
        }
    }

    /** 컨트롤러 진입 로그 (주로 HTTP 요청 정보 보강용) */
    @Before("org.hanihome.hanihomebe.global.aop.common.CommonPointCuts.inRestController()")
    public void logControllerEntry(JoinPoint jp) {
        String signature = jp.getSignature().toShortString();
        log.info("[Controller] {}", signature);
    }
}
