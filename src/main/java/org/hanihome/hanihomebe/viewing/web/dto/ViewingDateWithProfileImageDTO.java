package org.hanihome.hanihomebe.viewing.web.dto;

import org.hanihome.hanihomebe.viewing.domain.Viewing;

import java.time.LocalDateTime;


public record ViewingDateWithProfileImageDTO(
          Long id,

          LocalDateTime meetingDay,

          String myImageUrl,

          String counterpartImageUrl
) implements ViewingDTOByView {
    public static ViewingDateWithProfileImageDTO from(Viewing entity) {
        return new ViewingDateWithProfileImageDTO(
                entity.getId(),
                entity.getMeetingDay(),
                entity.getMember().getProfileImage(),
                entity.getProperty().getMember().getProfileImage()
        );
    }
}
