package org.hanihome.hanihomebe.property.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.property.application.service.PropertySearchService;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.property.web.dto.enums.PropertyViewType;
import org.hanihome.hanihomebe.property.web.dto.request.PropertySearchConditionDTO;
import org.hanihome.hanihomebe.property.web.dto.response.PropertyResponseDTO;
import org.hanihome.hanihomebe.viewing.web.enums.ViewingViewType;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@RequestMapping("/api/v1/properties")
@RestController
public class PropertySearchController {
    private final PropertySearchService propertySearchService;

    @GetMapping("/search")
    public List<?> searchProperties(
            @ParameterObject @Valid PropertySearchConditionDTO conditionDTO,
            Pageable pageable) {
        return propertySearchService.search(conditionDTO, PropertyViewType.SUMMARY);
    }
}
