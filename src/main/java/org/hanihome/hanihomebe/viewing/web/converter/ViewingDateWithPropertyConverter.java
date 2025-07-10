package org.hanihome.hanihomebe.viewing.web.converter;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.property.application.PropertyMapper;
import org.hanihome.hanihomebe.viewing.domain.Viewing;
import org.hanihome.hanihomebe.viewing.web.dto.PropertySummaryDTO;
import org.hanihome.hanihomebe.viewing.web.dto.ViewingDateWithPropertyDTO;
import org.hanihome.hanihomebe.viewing.web.enums.ViewingViewType;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ViewingDateWithPropertyConverter implements ViewingConverter<ViewingDateWithPropertyDTO>{
    private final PropertyMapper propertyMapper;

    @Override
    public ViewingViewType supports() {
        return ViewingViewType.DATE_WITH_PROPERTY;
    }

    @Override
    public ViewingDateWithPropertyDTO convert(Viewing viewing) {
        PropertySummaryDTO propertySummaryDTO = propertyMapper.toSummaryDTO(viewing.getProperty());
        return ViewingDateWithPropertyDTO.from(viewing.getMeetingDay(), propertySummaryDTO);
    }
}
