package org.hanihome.hanihomebe.property.application.service;

import org.hanihome.hanihomebe.item.application.OptionItemConverterForProperty;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.hanihome.hanihomebe.property.application.PropertyConverter;
import org.hanihome.hanihomebe.property.application.PropertyMapper;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.property.repository.PropertyRepository;
import org.hanihome.hanihomebe.property.web.dto.enums.PropertyViewType;
import org.hanihome.hanihomebe.property.web.dto.request.PropertySearchConditionDTO;
import org.hanihome.hanihomebe.property.web.dto.response.PropertyResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Transactional(readOnly = true)
@Service
public class PropertySearchService {
    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;
    private final OptionItemConverterForProperty optionItemConverter;
    private final Map<PropertyViewType, PropertyConverter<?>> propertyConverterMap = new EnumMap<>(PropertyViewType.class);

    public PropertySearchService(PropertyRepository propertyRepository, PropertyMapper propertyMapper, List<PropertyConverter<?>> propertyConverters, OptionItemConverterForProperty optionItemConverter) {
        this.propertyRepository = propertyRepository;
        this.propertyMapper = propertyMapper;
        this.optionItemConverter = optionItemConverter;
        propertyConverters.forEach(converter ->
                propertyConverterMap.put(converter.supports(), converter));
    }

    public <T> List<T> search(PropertySearchConditionDTO conditionDTO,
                                                      PropertyViewType view) {
        EnumMap<PropertySuperType, List<PropertyResponseDTO>> dtoMap = new EnumMap<>(PropertySuperType.class);
        for (PropertySuperType kind : PropertySuperType.values()) {
            dtoMap.put(kind, new ArrayList<>());
        }

        List<Property> findProperties = propertyRepository.search(conditionDTO);

        PropertyConverter<T> converterByView = (PropertyConverter<T>) getConverterByView(view);

        return findProperties.stream()
                .map(property -> converterByView.convert(property, getOptionItemResponseDTOs(property)))
                .toList();
    }

    private List<OptionItemResponseDTO> getOptionItemResponseDTOs(Property property) {
        List<OptionItemResponseDTO> optionItemsDTOs = optionItemConverter.toResponseDTO(property.getOptionItems());
        return optionItemsDTOs;
    }

    private PropertyConverter<?> getConverterByView(PropertyViewType view) {
        return propertyConverterMap.getOrDefault(view, propertyConverterMap.get(PropertyViewType.DEFAULT));
    }
}
