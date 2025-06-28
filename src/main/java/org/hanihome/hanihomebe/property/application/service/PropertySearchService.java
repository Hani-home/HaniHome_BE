package org.hanihome.hanihomebe.property.application.service;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.property.application.PropertyMapper;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.property.repository.PropertyRepository;
import org.hanihome.hanihomebe.property.repository.PropertySearchRepositoryImpl;
import org.hanihome.hanihomebe.property.web.dto.PropertySearchConditionDTO;
import org.hanihome.hanihomebe.property.web.dto.response.PropertyResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PropertySearchService {
    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;


    public Map<PropertySuperType, List<PropertyResponseDTO>> search(PropertySearchConditionDTO conditionDTO) {
        List<Property> findProperties = propertyRepository.search(conditionDTO);

        Map<PropertySuperType, List<PropertyResponseDTO>> dtoMap = findProperties.stream()
                .map(property -> propertyMapper.toResponseDto(property))
                .collect(Collectors.groupingBy(dto -> dto.kind(), Collectors.toList()));

        return dtoMap;
    }
}
