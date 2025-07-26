package org.hanihome.hanihomebe.viewing.web.dto;

import org.hanihome.hanihomebe.property.web.dto.response.summary.PropertySummaryDTO;
import org.hanihome.hanihomebe.viewing.domain.Viewing;

import java.time.LocalDateTime;

public record ViewingDateWithPropertyDTO(
        Long id,
        LocalDateTime meetingDay,
        PropertySummaryDTO property,
        String counterpartNickname,
        boolean canSeeViewingDetail
) implements ViewingDTOByView {
    public static ViewingDateWithPropertyDTO from(Viewing viewing,
                                                  PropertySummaryDTO property,
                                                  String counterpartNickname,
                                                  boolean canSeeViewingDetail
                                                  ) {
        return new ViewingDateWithPropertyDTO(viewing.getId(),
                viewing.getMeetingDay(),
                property,
                counterpartNickname,
                canSeeViewingDetail);
    }
}
