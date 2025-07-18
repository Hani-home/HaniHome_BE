package org.hanihome.hanihomebe.property.application.service;

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

@Transactional(readOnly = true)
@Service
public class PropertySearchService {
    private final PropertyRepository propertyRepository;
    private final PropertyConversionService propertyConversionService;

    public PropertySearchService(PropertyRepository propertyRepository,
                                 PropertyConversionService propertyConversionService
    ) {
        this.propertyRepository = propertyRepository;
        this.propertyConversionService = propertyConversionService;
    }

    public <T> List<T> search(PropertySearchConditionDTO conditionDTO,
                              PropertyViewType view
    ) {
        List<Property> findProperties = propertyRepository.search(conditionDTO);

        return propertyConversionService.convertProperties(findProperties, view);
    }

}
