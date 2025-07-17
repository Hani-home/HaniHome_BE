package org.hanihome.hanihomebe.metro.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.metro.domain.MetroStop;
import org.hanihome.hanihomebe.metro.repository.MetroStopRepository;
import org.hanihome.hanihomebe.metro.web.dto.FileParsedDTO;
import org.hanihome.hanihomebe.metro.web.dto.MetroStopPatchDTO;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
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
public class MetroStopSyncService {
    private final MetroStopRepository metroStopRepository;
    private final MetroStopFileService metroStopFileService;

    /**
     * 외부 API 호출 → 파일 저장 → stops.txt 파싱 → DB 동기화
     */
    @Transactional
    public void checkAndUpdateStops() throws IOException {
        FileParsedDTO fileParsed = metroStopFileService.loadFile();
        List<MetroStopPatchDTO> allDTOs = getMetroStopPatchDTOS(fileParsed);

        List<MetroStopPatchDTO> parentDTOs = filterParent(allDTOs);
        List<MetroStopPatchDTO> childDTOs  = filterChild(allDTOs);

        compareExistsToDTO(parentDTOs, childDTOs);
    }

    private void compareExistsToDTO(List<MetroStopPatchDTO> parentDTOs, List<MetroStopPatchDTO> childDTOs) {
        // 기존 DB 로드
        List<MetroStop> existingList = metroStopRepository.findAll();
        Map<String, MetroStop> existingMap = existingList.stream()
                .collect(Collectors.toMap(MetroStop::getBusinessStopId, s -> s));

        // DB와 동기화
        createOrUpdateParentStop(parentDTOs, existingMap);
        createOrUpdateChildStop(childDTOs, existingMap);

        // 삭제: DB에 존재하나 파싱 데이터에 없는 엔티티
        deleteDeprecatedMetroStop(existingMap);
    }

    private void deleteDeprecatedMetroStop(Map<String, MetroStop> existingMap) {
        List<MetroStop> toDelete = existingMap.values().stream().toList();
        metroStopRepository.deleteAll(toDelete);
    }

    private List<MetroStopPatchDTO> getMetroStopPatchDTOS(FileParsedDTO parsed) {
        List<String> lines = parsed.lines();
        String headerLine = parsed.headerLine();

        String[] headers = headerLine.split(",", -1);
        Map<String, Integer> idx = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            idx.put(headers[i].replace("\"", "").trim(), i);
        }

        log.info("index map: {}", idx.toString());

        // DTO 리스트 생성
        List<MetroStopPatchDTO> allDTOs = parseMetroStopDTOsFromLines(lines, idx);
        return allDTOs;
    }

    private static List<MetroStopPatchDTO> filterChild(List<MetroStopPatchDTO> allDTOs) {
        return allDTOs.stream()
                .filter(dto -> !dto.parentBusinessStopId().isBlank())
                .collect(Collectors.toList());
    }

    private static List<MetroStopPatchDTO> filterParent(List<MetroStopPatchDTO> allDTOs) {
        return allDTOs.stream()
                .filter(dto -> dto.parentBusinessStopId().isBlank())    // "" 또는 " "
                .collect(Collectors.toList());
    }

    // TODO: 내부 공통 함수 부분 리팩토링 가능해보임
    private void createOrUpdateChildStop(List<MetroStopPatchDTO> childDTOs, Map<String, MetroStop> existingMap) {
        List<MetroStop> toSave = new ArrayList<>();
        for (MetroStopPatchDTO dto : childDTOs) {
            MetroStop parent = metroStopRepository.findByBusinessStopId(dto.parentBusinessStopId())
                    .orElseThrow(() -> new CustomException(ServiceCode.PARENT_METRO_STOP_NOT_EXISTS));

            MetroStop entity = existingMap.get(dto.businessStopId());
            if (entity != null) {
                entity.update(dto);
                existingMap.remove(dto.businessStopId());
            } else {
                entity = MetroStop.createChild(
                        dto.businessStopId(), dto.stopName(), dto.stopLatitude(), dto.stopLongitude(),
                        dto.locationType(), parent, dto.wheelchairBoarding(), dto.platformCode()
                );
            }
            toSave.add(entity);
        }
        metroStopRepository.saveAll(toSave);
    }

    private void createOrUpdateParentStop(List<MetroStopPatchDTO> patchDTOs, Map<String, MetroStop> existingMap) {
        List<MetroStop> toSave = new ArrayList<>();
        for (MetroStopPatchDTO dto : patchDTOs) {
            MetroStop entity = existingMap.get(dto.businessStopId());
            if (entity != null) {
                entity.update(dto);
                existingMap.remove(dto.businessStopId());
            } else {
                entity = MetroStop.createParent(
                        dto.businessStopId(), dto.stopName(), dto.stopLatitude(), dto.stopLongitude(),
                        dto.locationType(), dto.wheelchairBoarding(), dto.platformCode()
                );
            }
            toSave.add(entity);
        }
        metroStopRepository.saveAll(toSave);
    }

    private static List<MetroStopPatchDTO> parseMetroStopDTOsFromLines(List<String> lines, Map<String, Integer> idx) {
        return lines.stream().map(line -> {

            List<String> parts = extractQuotedParts(line);

            log.info("parts: {}", parts.toString());

            return buildMetroStopDTO(parts, idx);
        }).collect(Collectors.toList());
    }

    private static MetroStopPatchDTO buildMetroStopDTO(List<String> parts, Map<String, Integer> idx) {
        String businessId = parts.get(idx.get("stop_id")).replace("\"", "");
        String stopName = parts.get(idx.get("stop_name")).replace("\"", "");
        BigDecimal lat = new BigDecimal(parts.get(idx.get("stop_lat")).replace("\"", ""));
        BigDecimal lon = new BigDecimal(parts.get(idx.get("stop_lon")).replace("\"", ""));
        String locationType = parts.get(idx.get("location_type")).replace("\"", "");
        String parentId = parts.get(idx.get("parent_station")).replace("\"", "");
        Boolean wheelchair = "1".equals(parts.get(idx.get("wheelchair_boarding")).replace("\"", ""));
        String platformCode = parts.get(idx.get("platform_code")).replace("\"", "");
        return new MetroStopPatchDTO(businessId, stopName, lat, lon, locationType, parentId, wheelchair, platformCode);
    }

    private static List<String> extractQuotedParts(String line) {
        List<String> parts = new ArrayList<>();

        Pattern csvPattern = Pattern.compile("\"([^\"]*)\"");
        Matcher matcher = csvPattern.matcher(line);
        while (matcher.find()) {
            parts.add(matcher.group(1).trim());
        }
        return parts;
    }


}
