package org.hanihome.hanihomebe.metro.application.service;

import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.metro.web.dto.FileParsedDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class MetroStopFileService {
    @Value("${nsw.sydney.metro.stop.storage.directory}")
    private String storagePath;

    public FileParsedDTO loadFile() throws IOException {
        Path base = Paths.get(storagePath);
        Path filePath = base.resolve("stops.txt");
        if (!Files.exists(filePath)) {
            throw new IllegalStateException("stops.txt not found in " + storagePath);
        }

        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        if (lines.isEmpty()) {
            throw new CustomException(ServiceCode.METRO_STOPS_FILE_IS_EMPTY);
        }
        // 파일 첫 시작부 BOM 제거 + 헤더 파싱
        String headerLine = lines.get(0).replace("\uFEFF", "");
        lines.remove(0);

        return FileParsedDTO.create(lines, headerLine);
    }
}
