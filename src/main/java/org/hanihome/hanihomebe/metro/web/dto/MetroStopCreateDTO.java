package org.hanihome.hanihomebe.metro.web.dto;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.hanihome.hanihomebe.metro.domain.MetroStop;

import java.math.BigDecimal;

public record MetroStopCreateDTO(
        String businessStopId,
        String stopName,
        BigDecimal stopLatitude,
        BigDecimal stopLongitude,
        String locationType,
        String parentBusinessStopId,
        boolean wheelchairBoarding,
        String platformCode
) {
}
