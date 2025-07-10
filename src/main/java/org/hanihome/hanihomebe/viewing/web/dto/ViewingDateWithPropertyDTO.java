package org.hanihome.hanihomebe.viewing.web.dto;

import java.time.LocalDateTime;

public record ViewingDateWithPropertyDTO(
        LocalDateTime meetingDay,
        PropertySummaryDTO property
) implements ViewingDTOByView {
    public static ViewingDateWithPropertyDTO from(LocalDateTime meetingDay, PropertySummaryDTO property) {
        return new ViewingDateWithPropertyDTO(meetingDay, property);
    }
}
