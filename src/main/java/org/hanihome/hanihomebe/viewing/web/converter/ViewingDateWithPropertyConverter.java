package org.hanihome.hanihomebe.viewing.web.converter;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.property.application.service.PropertyConversionService;
import org.hanihome.hanihomebe.property.web.dto.enums.PropertyViewType;
import org.hanihome.hanihomebe.property.web.dto.response.summary.PropertySummaryDTO;
import org.hanihome.hanihomebe.viewing.domain.Viewing;
import org.hanihome.hanihomebe.viewing.web.converter.context.ViewingConvertContext;
import org.hanihome.hanihomebe.viewing.web.dto.ViewingDateWithPropertyDTO;
import org.hanihome.hanihomebe.viewing.web.enums.ViewingViewType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ViewingDateWithPropertyConverter implements ViewingConverter<ViewingDateWithPropertyDTO>{
    private final PropertyConversionService propertyConversionService;

    @Override
    public ViewingViewType supports() {
        return ViewingViewType.DATE_WITH_PROPERTY;
    }

    @Override
    public ViewingDateWithPropertyDTO convert(ViewingConvertContext viewingConvertContext) {
        Viewing viewing = viewingConvertContext.getViewing();

        PropertySummaryDTO propertySummaryDTO = getPropertySummaryDTO(viewing);

        return ViewingDateWithPropertyDTO.from(viewing.getMeetingDay(), propertySummaryDTO);
    }

    private PropertySummaryDTO getPropertySummaryDTO(Viewing viewing) {
        Optional<Object> optionalPropertySummaryDTO = propertyConversionService.convertProperties(List.of(viewing.getProperty()), PropertyViewType.SUMMARY)
                .stream()
                .findFirst();
        PropertySummaryDTO propertySummaryDTO = (PropertySummaryDTO) optionalPropertySummaryDTO
                .orElseThrow(() -> new CustomException(ServiceCode.PROPERTY_IN_VIEWING_CONVERT_EXCEPTION));
        return propertySummaryDTO;
    }

}
