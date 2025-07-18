package org.hanihome.hanihomebe.metro.application.service;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.metro.domain.MetroStop;
import org.hanihome.hanihomebe.metro.domain.NearestMetroStop;
import org.hanihome.hanihomebe.metro.repository.MetroStopRepository;
import org.hanihome.hanihomebe.metro.repository.NearestMetroStopRepository;
import org.hanihome.hanihomebe.metro.web.dto.nearest.NearestMetroStopProjectionDTO;
import org.hanihome.hanihomebe.property.domain.Property;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NearestMetroStopService {
    private final NearestMetroStopRepository nearestMetroStopRepository;
    private final MetroStopRepository metroStopRepository;

    @Transactional
    public NearestMetroStop create(Property property) {
        NearestMetroStopProjectionDTO nearestMetroAndDistance = metroStopRepository.findNearestMetroAndDistance(property.getRegion().getLatitude(), property.getRegion().getLongitude());

        MetroStop findMetroStop = metroStopRepository.findById(nearestMetroAndDistance.getId()).orElseThrow(() -> new CustomException(ServiceCode.METRO_STOP_NOT_EXISTS));
        Double distance = nearestMetroAndDistance.getDistance();

        return nearestMetroStopRepository.save(NearestMetroStop.create(findMetroStop, property, distance));
    }
}
