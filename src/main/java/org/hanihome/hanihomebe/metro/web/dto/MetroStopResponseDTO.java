package org.hanihome.hanihomebe.metro.web.dto;

import org.hanihome.hanihomebe.metro.domain.MetroStop;

import java.math.BigDecimal;

public record MetroStopResponseDTO(
        Long id,
        String businessStopId,
        String stopName,
        BigDecimal stopLatitude,
        BigDecimal stopLongitude,
        String locationType,
        String parentBusinessStopId,
        boolean wheelchairBoarding,
        String platformCode
) {
    public static MetroStopResponseDTO from(MetroStop metroStop) {
        return new MetroStopResponseDTO(
                metroStop.getId(),
                metroStop.getBusinessStopId(),
                metroStop.getStopName(),
                metroStop.getStopLatitude(),
                metroStop.getStopLongitude(),
                metroStop.getLocationType(),
                metroStop.getParentStop() != null ? metroStop.getParentStop().getBusinessStopId() : null,
                metroStop.getWheelchairBoarding(),
                metroStop.getPlatformCode()
        );
    }
}
