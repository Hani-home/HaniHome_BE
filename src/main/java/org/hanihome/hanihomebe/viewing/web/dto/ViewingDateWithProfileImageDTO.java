package org.hanihome.hanihomebe.viewing.web.dto;

import org.hanihome.hanihomebe.viewing.domain.Viewing;

import java.time.LocalDateTime;


public record ViewingDateWithProfileImageDTO(
          Long id,

          LocalDateTime meetingDay,

          String counterpartImageUrl,

          String propertyThumbnailUrl

) implements ViewingDTOByView {
    public static ViewingDateWithProfileImageDTO from(Viewing entity, String counterpartImageUrl) {
        return new ViewingDateWithProfileImageDTO(
                entity.getId(),
                entity.getMeetingDay(),
                counterpartImageUrl,
                entity.getProperty().getThumbnailUrl()
        );
    }
}
