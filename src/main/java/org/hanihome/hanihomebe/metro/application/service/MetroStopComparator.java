package org.hanihome.hanihomebe.metro.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.metro.domain.MetroStop;
import org.hanihome.hanihomebe.metro.repository.MetroStopRepository;
import org.hanihome.hanihomebe.metro.web.dto.FileParsedDTO;
import org.hanihome.hanihomebe.metro.web.dto.MetroStopPatchDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// TODO: 레포지토리를 가지고 있으니 서비스계층으로 올리는 것이 맞아보임. 또한 MetroService에서 생성과 수정하는 함수는 없애고
//   아래의 내용으로 통합하는것이 필요해보임
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Component
public class MetroStopComparator {
    private final MetroStopRepository metroStopRepository;

    @Value("${nsw.sydney.metro.stop.storage.directory}")
    private String storagePath;

    /**
     * 외부 API 호출 → 파일 저장 → stops.txt 파싱 → DB 동기화
     */
    @Transactional
    public void checkAndUpdateStops() throws IOException {

        // 1) 저장된 파일 로드
        FileParsedDTO parsed = loadFile(storagePath);
        List<String> lines = parsed.lines();
        String headerLine = parsed.headerLine();

        List<MetroStopPatchDTO> allDTOs = parseMetroStops(headerLine, lines);

        // DTO 분류: 부모 리스트 (parentId empty), 자식 리스트 (parentId not empty)
        List<MetroStopPatchDTO> parentDTOs = allDTOs.stream()
                .filter(dto -> dto.parentBusinessStopId().isBlank())    // "" 또는 " "
                .collect(Collectors.toList());
        List<MetroStopPatchDTO> childDTOs  = allDTOs.stream()
                .filter(dto -> !dto.parentBusinessStopId().isBlank())
                .collect(Collectors.toList());

        // 기존 DB 로드
        List<MetroStop> existingList = metroStopRepository.findAll();
        Map<String, MetroStop> existingMap = existingList.stream()
                .collect(Collectors.toMap(MetroStop::getBusinessStopId, s -> s));

        // 처리된 ID 집합
        Set<String> processedIds = new HashSet<>();

        // 3) 부모 엔티티부터 처리
        updateParentStopDTO(parentDTOs, existingMap, processedIds);

        // 4) 자식 엔티티 처리 (부모 참조 적용)
        updateChildStopDTO(childDTOs, existingMap, processedIds);

        // 5) 삭제: DB에 존재하나 파싱 데이터에 없는 엔티티
        for (MetroStop toDelete : existingMap.values()) {
            if (!processedIds.contains(toDelete.getBusinessStopId())) {
                metroStopRepository.delete(toDelete);
            }
        }
    }
    // TODO: 내부 공통 함수 부분 리팩토링 가능해보임
    private void updateChildStopDTO(List<MetroStopPatchDTO> childDTOs, Map<String, MetroStop> existingMap, Set<String> processedIds) {
        for (MetroStopPatchDTO dto : childDTOs) {
            MetroStop parent = metroStopRepository.findByBusinessStopId(dto.parentBusinessStopId())
                    .orElseThrow(() -> new IllegalStateException("Parent stop not found: " + dto.parentBusinessStopId()));

            MetroStop entity = existingMap.get(dto.businessStopId());
            if (entity != null) {
                entity.update(dto);
                metroStopRepository.save(entity);
                existingMap.remove(dto.businessStopId());
            } else {
                entity = MetroStop.createChild(
                        dto.businessStopId(), dto.stopName(), dto.stopLatitude(), dto.stopLongitude(),
                        dto.locationType(), parent, dto.wheelchairBoarding(), dto.platformCode()
                );
                metroStopRepository.save(entity);
            }
            processedIds.add(dto.businessStopId());
        }
    }

    private void updateParentStopDTO(List<MetroStopPatchDTO> patchDTOs, Map<String, MetroStop> existingMap, Set<String> processedIds) {
        for (MetroStopPatchDTO dto : patchDTOs) {
            MetroStop entity = existingMap.get(dto.businessStopId());
            if (entity != null) {
                entity.update(dto);
                metroStopRepository.save(entity);
                existingMap.remove(dto.businessStopId());
            } else {
                entity = MetroStop.createParent(
                        dto.businessStopId(), dto.stopName(), dto.stopLatitude(), dto.stopLongitude(),
                        dto.locationType(), dto.wheelchairBoarding(), dto.platformCode()
                );
                metroStopRepository.save(entity);
            }
            processedIds.add(dto.businessStopId());
        }
    }

    private List<MetroStopPatchDTO> parseMetroStops(String headerLine, List<String> lines) {
        // 헤더 인덱스 맵 구축
        String[] headers = headerLine.split(",", -1);
        Map<String, Integer> idx = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            idx.put(headers[i].replace("\"", "").trim(), i);
        }

        log.info("index map: {}", idx.toString());

        // DTO 리스트 생성
        List<MetroStopPatchDTO> allDTOs = lines.stream().map(line -> {

            List<String> parts = new ArrayList<>();

            Pattern csvPattern = Pattern.compile("\"([^\"]*)\"");
            Matcher matcher = csvPattern.matcher(line);
            while (matcher.find()) {
                parts.add(matcher.group(1).trim());
            }

            log.info("parts: {}", parts.toString());

            String businessId = parts.get(idx.get("stop_id")).replace("\"", "");
            String stopName = parts.get(idx.get("stop_name")).replace("\"", "");
            BigDecimal lat = new BigDecimal(parts.get(idx.get("stop_lat")).replace("\"", ""));
            BigDecimal lon = new BigDecimal(parts.get(idx.get("stop_lon")).replace("\"", ""));
            String locationType = parts.get(idx.get("location_type")).replace("\"", "");
            String parentId = parts.get(idx.get("parent_station")).replace("\"", "");
            Boolean wheelchair = "1".equals(parts.get(idx.get("wheelchair_boarding")).replace("\"", ""));
            String platformCode = parts.get(idx.get("platform_code")).replace("\"", "");
            return new MetroStopPatchDTO(businessId, stopName, lat, lon, locationType, parentId, wheelchair, platformCode);
        }).collect(Collectors.toList());
        return allDTOs;
    }

    private FileParsedDTO loadFile(String storagePath) throws IOException {
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
