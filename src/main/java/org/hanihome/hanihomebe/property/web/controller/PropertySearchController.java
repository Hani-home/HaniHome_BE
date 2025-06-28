package org.hanihome.hanihomebe.property.web.controller;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.property.application.service.PropertySearchService;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.property.web.dto.PropertySearchConditionDTO;
import org.hanihome.hanihomebe.property.web.dto.response.PropertyResponseDTO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@RequestMapping("/api/v1/properties")
@RestController
public class PropertySearchController {
    private final PropertySearchService propertySearchService;

    @GetMapping("/search")
    public Map<PropertySuperType,List<PropertyResponseDTO>> searchProperties(
            @ParameterObject PropertySearchConditionDTO conditionDTO,
            Pageable pageable) {
        return propertySearchService.search(conditionDTO);
    }
}
