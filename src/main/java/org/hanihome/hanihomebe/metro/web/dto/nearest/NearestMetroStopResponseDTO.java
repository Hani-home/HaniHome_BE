package org.hanihome.hanihomebe.metro.web.dto.nearest;

import org.hanihome.hanihomebe.metro.domain.MetroStop;
import org.hanihome.hanihomebe.metro.domain.NearestMetroStop;

public record NearestMetroStopResponseDTO(
        Long metroStopId,
        String name,
        Double distanceFromStation
) {
    public static NearestMetroStopResponseDTO from(NearestMetroStop nearestMetroStop) {
        MetroStop metroStop = nearestMetroStop.getMetroStop();
        return new NearestMetroStopResponseDTO(metroStop.getId(), metroStop.getStopName(), nearestMetroStop.getDistance());
    }
}