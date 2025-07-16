package org.hanihome.hanihomebe.viewing.web.converter;

import org.hanihome.hanihomebe.viewing.domain.Viewing;
import org.hanihome.hanihomebe.viewing.web.converter.context.ViewingConvertContext;
import org.hanihome.hanihomebe.viewing.web.dto.ViewingDateWithProfileImageDTO;
import org.hanihome.hanihomebe.viewing.web.enums.ViewingViewType;
import org.springframework.stereotype.Component;

/// Map<String, Object> Extra에 "requesterId" 필요
@Component
public class ViewingDateWithProfileImageConverter implements ViewingConverter<ViewingDateWithProfileImageDTO>{
    @Override
    public ViewingViewType supports() {
        return ViewingViewType.DATE_PROFILE;
    }

    @Override
    public ViewingDateWithProfileImageDTO convert(ViewingConvertContext viewingConvertContext) {
        Long requesterId = viewingConvertContext.getExtra("requesterId");

        String counterpartImageUrl = getCounterPartImageUrl(requesterId, viewingConvertContext.getViewing());

        return ViewingDateWithProfileImageDTO.from(viewingConvertContext.getViewing(), counterpartImageUrl);
    }

    private String getCounterPartImageUrl(Long requesterId, Viewing viewing) {
        if (requesterIsGuest(requesterId, viewing)) {
            return getHostProfileImageUrl(viewing);
        } else {
            return getGuestProfileImageUrl(viewing);
        }
    }

    private static String getGuestProfileImageUrl(Viewing viewing) {
        return viewing.getMember().getProfileImage();
    }

    private static String getHostProfileImageUrl(Viewing viewing) {
        return viewing.getProperty().getMember().getProfileImage();
    }

    private static boolean requesterIsGuest(Long requesterId, Viewing viewing) {
        return requesterId.equals(viewing.getMember().getId());
    }

}
