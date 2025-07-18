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

    public PropertyConversionService(List<PropertyConverter<?>> converterList,
                                     NearestMetroStopRepository nearestMetroStopRepository,
                                     OptionItemConverterForProperty optionItemConverter
    ) {
        this.propertyConverterMap = converterList.stream()
                .collect(Collectors.toMap(PropertyConverter::supports, Function.identity()));
        this.nearestMetroStopRepository = nearestMetroStopRepository;
        this.optionItemConverter = optionItemConverter;
    }


    public <T> List<T> convertProperties(List<Property> properties, PropertyViewType viewType) {
        return convertPropertyListToDTO(properties, viewType);
    }

    public <T> T convertProperty(Property property, PropertyViewType viewType) {
        PropertyConverter<T> converter = getConverterByView(viewType);

        NearestMetroStop nearestMetroStop = getNearestMetroStop(property);

        return converter.convert(preparePropertyConvertContext(property, nearestMetroStop));
    }

    private <T> PropertyConverter<T> getConverterByView(PropertyViewType view) {
        PropertyConverter<?> converter = propertyConverterMap.getOrDefault(view, propertyConverterMap.get(PropertyViewType.DEFAULT));
        return (PropertyConverter<T>) converter;
    }

    private NearestMetroStop getNearestMetroStop(Property property) {
        return nearestMetroStopRepository.findByProperty(property)
                .orElseThrow(() -> new CustomException(ServiceCode.NEAREST_METRO_STOP_NOT_EXISTS));
    }

    private <T> List<T> convertPropertyListToDTO(List<Property> properties, PropertyViewType viewType) {
        return properties
                .stream()
                .map(property -> this.<T>convertProperty(property, viewType))
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