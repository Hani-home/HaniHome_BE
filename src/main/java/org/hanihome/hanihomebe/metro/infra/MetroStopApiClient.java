package org.hanihome.hanihomebe.metro.infra;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
@Component
public class MetroStopApiClient {


    private final String metroStopUrl;

    private final String apiKey;

    private final String storagePath;

    private final RestTemplate restTemplate;

    public MetroStopApiClient(
            RestTemplateBuilder builder,
            @Value("${nsw.sydney.metro.stop.url}") String metroStopUrl,
            @Value("${nsw.api.key}") String apiKey,
            @Value("${nsw.sydney.metro.stop.storage.directory}") String storagePath
    ) {
        this.restTemplate = builder.build();
        this.metroStopUrl = metroStopUrl;
        this.apiKey = apiKey;
        this.storagePath = storagePath;
    }

    /**
     * 1) API 호출 후 바이트 배열 획득
     * 2) ZIP 여부 검사
     * 3) ZIP 이면 압축 해제, 아니면 단일 파일로 저장
     */
    public void fetchAndStoreMetroStops() throws IOException {
        fetchMetroStops();
        storeMetroStops();
    }

    public void storeMetroStops() throws IOException {
        log.info("sydney metro fetch start");
        byte[] data = fetchMetroStops();

        Path base = Paths.get(storagePath);
        Files.createDirectories(base);

        /// 별도에 미리 쓰기 권한 줘야함
        if (isZip(data)) {
            try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(data))) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    Path out = base.resolve(entry.getName());
                    if (entry.isDirectory()) {
                        Files.createDirectories(out);
                    } else {
                        Files.createDirectories(out.getParent());
                        // 덮어쓰기
                        Files.copy(zis, out, StandardCopyOption.REPLACE_EXISTING);
                    }
                    zis.closeEntry();
                }
            }
            log.info("sydney metro fetch end");
            log.info("sydney metro schedule stored at {}", storagePath);
            log.info("sydney metro schedule file count: {}", Files.walk(base).count());
            log.info("sydney metro schedule file size: {}", Files.walk(base).mapToLong(p -> p.toFile().length()).sum());
            log.info("sydney metro schedule file list: {}", Files.walk(base).map(p -> p.toString()).toList());
            log.info("sydney metro schedule file list (dir): {}", Files.walk(base).filter(p -> p.toFile().isDirectory()).map(p -> p.toString()).toList());
            log.info("sydney metro schedule file list (file): {}", Files.walk(base).filter(p -> p.toFile().isFile()).map(p -> p.toString()).toList());
        } else {
            // ZIP이 아니면 단일 파일로 저장
            Path out = base.resolve("metro_schedule.bin");
            Files.write(out, data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }

    }

    private byte[] fetchMetroStops() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
        headers.set("Authorization", "apikey " + apiKey);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        log.info("sydney metro url: {}", metroStopUrl);
        ResponseEntity<byte[]> response = restTemplate.exchange(
                metroStopUrl,
                HttpMethod.GET,
                request,
                byte[].class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new IllegalStateException(
                    "Metro schedule fetch failed: HTTP " + response.getStatusCode()
            );
        }
    }

    /** 앞 4바이트 PK\x03\x04 로 ZIP 여부 간단 검사 */
    private boolean isZip(byte[] data) {
        return data.length >= 4
                && data[0] == 0x50
                && data[1] == 0x4B
                && data[2] == 0x03
                && data[3] == 0x04;
    }
}
