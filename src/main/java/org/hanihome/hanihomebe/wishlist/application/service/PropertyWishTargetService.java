package org.hanihome.hanihomebe.wishlist.application.service;

import org.hanihome.hanihomebe.item.application.converter.OptionItemConverterForProperty;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.hanihome.hanihomebe.property.application.converter.PropertyMapper;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.repository.PropertyRepository;
import org.hanihome.hanihomebe.property.web.dto.response.PropertyResponseDTO;
import org.hanihome.hanihomebe.wishlist.domain.enums.WishTargetType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyWishTargetService implements WishTargetService{
    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;
    private final OptionItemConverterForProperty optionItemConverter;

    public PropertyWishTargetService(PropertyRepository propertyRepository, PropertyMapper propertyMapper, OptionItemConverterForProperty optionItemConverter) {
        this.propertyRepository = propertyRepository;
        this.propertyMapper = propertyMapper;
        this.optionItemConverter = optionItemConverter;
    }

    @Override
    public WishTargetType getTargetType() {
        return WishTargetType.PROPERTY;
    }

    @Override
    public List<PropertyResponseDTO> getTargetDTOs(List<Long> targetIds) {
        return propertyRepository.findAllById(targetIds).stream()
                .map(property -> propertyMapper.toResponseDTO(property, getOptionItemResponseDTOS(property)))
                .toList();
    }

    private List<OptionItemResponseDTO> getOptionItemResponseDTOS(Property property) {
        List<OptionItemResponseDTO> optionItemsDTOs = optionItemConverter.toOptionItemResponseDTO(property.getOptionItems());
        return optionItemsDTOs;
    }

}
