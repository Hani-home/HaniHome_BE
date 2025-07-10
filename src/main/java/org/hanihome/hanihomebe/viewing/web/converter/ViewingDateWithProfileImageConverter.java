package org.hanihome.hanihomebe.viewing.web.converter;

import org.hanihome.hanihomebe.viewing.domain.Viewing;
import org.hanihome.hanihomebe.viewing.web.dto.ViewingDateWithProfileImageDTO;
import org.hanihome.hanihomebe.viewing.web.enums.ViewingViewType;
import org.springframework.stereotype.Component;

@Component
public class ViewingDateWithProfileImageConverter implements ViewingConverter<ViewingDateWithProfileImageDTO>{
    @Override
    public ViewingViewType supports() {
        return ViewingViewType.DATE_PROFILE;
    }

    @Override
    public ViewingDateWithProfileImageDTO convert(Viewing viewing) {
        return ViewingDateWithProfileImageDTO.from(viewing);
    }
}
