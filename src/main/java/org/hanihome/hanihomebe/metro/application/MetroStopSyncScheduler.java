package org.hanihome.hanihomebe.metro.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.metro.application.service.MetroStopSyncService;
import org.hanihome.hanihomebe.metro.infra.MetroStopApiClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class MetroStopSyncScheduler {
    private final MetroStopSyncService metroStopSyncService;
    private final MetroStopApiClient metroStopApiClient;

    @Scheduled(cron = "0 0 0 * * *", zone = "UTC")    // 매일 00시
    public void synchronizeStops() {
        log.info("지하철역 동기화 스케줄링 시작");
        try {
            metroStopApiClient.fetchAndStoreMetroStops();
            metroStopSyncService.checkAndUpdateStops();
        } catch (IllegalStateException | IOException e) {
            throw new CustomException(ServiceCode.METRO_STOP_SYNCHRONIZE_FAILED, e);
        }

        log.info("지하철역 동기화 스케줄링 종료");
    }
}
