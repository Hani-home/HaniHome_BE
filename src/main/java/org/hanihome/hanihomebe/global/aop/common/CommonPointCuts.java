package org.hanihome.hanihomebe.global.aop.common;

import org.aspectj.lang.annotation.Pointcut;

/// 알림 전송, 스케줄링 작업에도 로깅남기는게 좋다.
/// 알림 전송 성공 ,실패 등의 내용은 실제 함수 안에서만 알 수 있기에 내부에서 로그를 찍는듯
public class CommonPointCuts {
    /** 서비스 계층의 모든 public 메서드 */
    @Pointcut("execution(public * org.hanihome.hanihomebe..service..*(..))")
    public void inServiceLayer() {}

    /** 컨트롤러(@RestController)의 모든 메서드 */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void inRestController() {}
}
