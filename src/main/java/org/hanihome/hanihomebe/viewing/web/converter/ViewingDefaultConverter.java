package org.hanihome.hanihomebe.viewing.web.converter;

import org.hanihome.hanihomebe.viewing.web.converter.context.ViewingConvertContext;
import org.hanihome.hanihomebe.viewing.web.dto.ViewingResponseDTO;
import org.hanihome.hanihomebe.viewing.web.enums.ViewingViewType;
import org.springframework.stereotype.Component;

@Component
public class ViewingDefaultConverter implements ViewingConverter<ViewingResponseDTO>{
    @Override
    public ViewingViewType supports() {
        return ViewingViewType.DEFAULT;
    }

    @Override
    public ViewingResponseDTO convert(ViewingConvertContext viewingConvertContext) {
        return ViewingResponseDTO.from(viewingConvertContext.getViewing());
    }
}
