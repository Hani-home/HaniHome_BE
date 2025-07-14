package org.hanihome.hanihomebe.viewing.web.dto;

import org.hanihome.hanihomebe.property.web.dto.response.summary.PropertySummaryDTO;

import java.time.LocalDateTime;

public record ViewingDateWithPropertyDTO(
        LocalDateTime meetingDay,
        PropertySummaryDTO property
) implements ViewingDTOByView {
    public static ViewingDateWithPropertyDTO from(LocalDateTime meetingDay, PropertySummaryDTO property) {
        return new ViewingDateWithPropertyDTO(meetingDay, property);
    }
}
