package org.hanihome.hanihomebe.metro.web.dto;

import java.math.BigDecimal;

public record MetroStopPatchDTO (
        String businessStopId,
        String stopName,
        BigDecimal stopLatitude,
        BigDecimal stopLongitude,
        String locationType,
        String parentBusinessStopId,
        Boolean wheelchairBoarding,
        String platformCode
){

}
