package org.hanihome.hanihomebe.global.aop.performance;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceMonitoringAspect {

    private final MeterRegistry meterRegistry;

    public PerformanceMonitoringAspect(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Around("execution(public * org.hanihome.hanihomebe..service..*(..))")
    public Object monitorPerformance(ProceedingJoinPoint pjp) throws Throwable {
        String metricName = "service.method.execution.time";   // 측정할 메트릭
        String[] tags = new String[] {
                "method", pjp.getSignature().toShortString()
        };  // 시간 측정할 메서드 이름 입력

        Timer.Sample timer = Timer.start(meterRegistry);
        try {
            return pjp.proceed();
        } finally {
            timer.stop(Timer.builder(metricName)
                    .description("Service method execution time")
                    .tags(tags)
                    .register(meterRegistry));
        }
    }
}
