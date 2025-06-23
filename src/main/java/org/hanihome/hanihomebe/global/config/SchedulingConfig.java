package org.hanihome.hanihomebe.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@EnableScheduling
@Configuration
public class SchedulingConfig {
    /**
     * TODO:
     *   1. 스케줄링과 작업실행을 분리.
     *   장: 작업은 비동기로 처리 가능, 작업 시간이 길어져도 다른 작업 실행에 영향없음
     *   단: 에러 핸들링
     *   2. 스프링 배치
     */
    // 주의: 서버 종료시 실행중인/예약된 작업 중지
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        scheduler.setThreadNamePrefix("Task-Scheduler-");
//        scheduler.setWaitForTasksToCompleteOnShutdown(true);    //delayed queue에 등록된 작업이 끝날 때까지 jvm 종료 안함
//        scheduler.setAwaitTerminationSeconds(60);
        scheduler.initialize();
        return scheduler;
    }
}
