package org.hanihome.hanihomebe.viewing.web.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ViewingBelongsToPropertyDTO(
        Long viewingId,
        Long guestId,
        String guestNickName,
        String guestThumbnailUrl,
        String propertyThumbnailUrl,
        LocalDate meetingDate,
        LocalTime meetingTime
) implements ViewingDTOByView{
    public static ViewingBelongsToPropertyDTO create(Long viewingId, Long guestId, String guestNickName, String guestThumbnailUrl, String propertyThumbnailUrl, LocalDate meetingDate, LocalTime meetingTime) {
        return new ViewingBelongsToPropertyDTO(
                viewingId,
                guestId,
                guestNickName,
                guestThumbnailUrl,
                propertyThumbnailUrl,
                meetingDate,
                meetingTime);
    }
}
