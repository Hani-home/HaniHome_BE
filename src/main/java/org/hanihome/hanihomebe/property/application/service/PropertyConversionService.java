package org.hanihome.hanihomebe.property.application.service;// PropertyConversionService.java (이전 답변에서 제공된 코드)

import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.item.application.OptionItemConverterForProperty;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.hanihome.hanihomebe.metro.domain.NearestMetroStop;
import org.hanihome.hanihomebe.metro.repository.NearestMetroStopRepository;
import org.hanihome.hanihomebe.metro.web.dto.nearest.NearestMetroStopResponseDTO;
import org.hanihome.hanihomebe.property.application.converter.PropertyConvertContext;
import org.hanihome.hanihomebe.property.application.converter.PropertyConverter;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.web.dto.enums.PropertyViewType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PropertyConversionService {

    private final Map<PropertyViewType, PropertyConverter<?>> propertyConverterMap;
    private final NearestMetroStopRepository nearestMetroStopRepository;
    private final OptionItemConverterForProperty optionItemConverter;

    public PropertyConversionService(List<PropertyConverter<?>> converterList, NearestMetroStopRepository nearestMetroStopRepository, OptionItemConverterForProperty optionItemConverter) {
        this.propertyConverterMap = converterList.stream()
                .collect(Collectors.toMap(PropertyConverter::supports, Function.identity()));
        this.nearestMetroStopRepository = nearestMetroStopRepository;
        this.optionItemConverter = optionItemConverter;
    }


    public <T> List<T> convertProperties(List<Property> properties, PropertyViewType viewType) {
        PropertyConverter<T> converter = (PropertyConverter<T>) getConverterByView(viewType);

        return convertPropertyListToDTO(properties, converter);

    }

    private PropertyConverter<?> getConverterByView(PropertyViewType view) {
        return propertyConverterMap.getOrDefault(view, propertyConverterMap.get(PropertyViewType.DEFAULT));
    }

    private <T> List<T> convertPropertyListToDTO(List<Property> properties, PropertyConverter<T> converter) {
        return properties
                .stream()
                .map(property ->
                {
                    NearestMetroStop nearestMetroStop = nearestMetroStopRepository.findByProperty(property)
                            .orElseThrow(() -> new CustomException(ServiceCode.NEAREST_METRO_STOP_NOT_EXISTS));

                    return converter.convert(preparePropertyConvertContext(property, nearestMetroStop));
                })
                .collect(Collectors.toList());
    }

    private PropertyConvertContext preparePropertyConvertContext(Property property, NearestMetroStop nearestMetroStop) {
        return PropertyConvertContext.create(property, getOptionItemResponseDTOS(property), NearestMetroStopResponseDTO.from(nearestMetroStop));
    }

    private List<OptionItemResponseDTO> getOptionItemResponseDTOS(Property property) {
        List<OptionItemResponseDTO> optionItemsDTOs = optionItemConverter.toResponseDTO(property.getOptionItems());
        return optionItemsDTOs;
    }
}