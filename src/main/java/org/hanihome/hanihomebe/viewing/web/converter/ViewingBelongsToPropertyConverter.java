package org.hanihome.hanihomebe.viewing.web.converter;

import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.viewing.domain.Viewing;
import org.hanihome.hanihomebe.viewing.web.converter.context.ViewingConvertContext;
import org.hanihome.hanihomebe.viewing.web.dto.ViewingBelongsToPropertyDTO;
import org.hanihome.hanihomebe.viewing.web.enums.ViewingViewType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ViewingBelongsToPropertyConverter implements ViewingConverter<ViewingBelongsToPropertyDTO>{
    @Override
    public ViewingViewType supports() {
        return ViewingViewType.BELONGS_TO_PROPERTY;
    }

    @Override
    public ViewingBelongsToPropertyDTO convert(ViewingConvertContext viewingConvertContext) {
        Viewing viewing = viewingConvertContext.getViewing();
        Member guest = viewing.getMember();
        Property property = viewing.getProperty();
        LocalDateTime meetingDay = viewing.getMeetingDay();

        return ViewingBelongsToPropertyDTO.create(
                viewing.getId(),
                guest.getId(),
                guest.getNickname(),
                guest.getProfileImage(),
                property.getThumbnailUrl(),
                meetingDay.toLocalDate(),
                meetingDay.toLocalTime()
        );
    }
}
