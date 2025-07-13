package org.hanihome.hanihomebe.metro.application.service;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.metro.domain.MetroStop;
import org.hanihome.hanihomebe.metro.repository.MetroStopRepository;
import org.hanihome.hanihomebe.metro.web.dto.MetroStopCreateDTO;
import org.hanihome.hanihomebe.metro.web.dto.MetroStopPatchDTO;
import org.hanihome.hanihomebe.metro.web.dto.MetroStopResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MetroStopService {
    private final MetroStopRepository metroStopRepository;

    // 등록
    @Transactional
    public Long createStop(MetroStopCreateDTO dto) {
        String parentBusinessStopId = dto.parentBusinessStopId();
        if (parentBusinessStopId == null || parentBusinessStopId.equals("")) {
            MetroStop entity = MetroStop.createParent(dto.businessStopId(),
                    dto.stopName(),
                    dto.stopLatitude(),
                    dto.stopLongitude(),
                    dto.locationType(),
                    dto.wheelchairBoarding(),
                    dto.platformCode()
            );
            return metroStopRepository.save(entity)
                    .getId();
        } else {
            MetroStop parentStop = metroStopRepository.findByBusinessStopId(parentBusinessStopId)
                    .orElseThrow(() -> new CustomException(ServiceCode.METRO_STOP_NOT_EXISTS));
            MetroStop entity = MetroStop.createChild(dto.businessStopId(),
                    dto.stopName(),
                    dto.stopLatitude(),
                    dto.stopLongitude(),
                    dto.locationType(),
                    parentStop,
                    dto.wheelchairBoarding(),
                    dto.platformCode()
            );

            return metroStopRepository.save(entity)
                    .getId();
        }
    }

    // 조회 (전체)
    public List<MetroStopResponseDTO> getAllStops() {
        return metroStopRepository.findAll()
                .stream()
                .map(stop -> MetroStopResponseDTO.from(stop))
                .toList();
    }

    // 조회 (단건)
    public Optional<MetroStop> getStopById(Long stopId) {
        return metroStopRepository.findById(stopId);
    }

    // 수정
    @Transactional
    public MetroStopResponseDTO updateStop(Long stopId, MetroStopPatchDTO dto) {
        MetroStop findStop = metroStopRepository.findById(stopId)
                .orElseThrow(() -> new CustomException(ServiceCode.METRO_STOP_NOT_EXISTS));

        // parentStop 유지
        if (dto.parentBusinessStopId() == null) {
            findStop.update(dto);
        }
        // parentStop 제거
        else if (dto.parentBusinessStopId().equals("")) {
            findStop.updateParent(null);
            findStop.update(dto);
        }
        // parentStop 변경
        else {
            MetroStop findParent = metroStopRepository.findByBusinessStopId(dto.parentBusinessStopId())
                    .orElseThrow(() -> new CustomException(ServiceCode.METRO_STOP_NOT_EXISTS));

            findStop.updateParent(findParent);
            findStop.update(dto);
        }

        metroStopRepository.save(findStop);
        return MetroStopResponseDTO.from(findStop);
    }

    // 삭제
    @Transactional
    public void deleteStop(Long stopId) {
        metroStopRepository.deleteById(stopId);
    }


}
