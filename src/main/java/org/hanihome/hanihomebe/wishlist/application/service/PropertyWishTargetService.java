package org.hanihome.hanihomebe.wishlist.application.service;

import org.hanihome.hanihomebe.property.application.PropertyMapper;
import org.hanihome.hanihomebe.property.repository.PropertyRepository;
import org.hanihome.hanihomebe.property.web.dto.response.PropertyResponseDTO;
import org.hanihome.hanihomebe.wishlist.domain.enums.WishTargetType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyWishTargetService implements WishTargetService{
    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;

    public PropertyWishTargetService(PropertyRepository propertyRepository, PropertyMapper propertyMapper) {
        this.propertyRepository = propertyRepository;
        this.propertyMapper = propertyMapper;
    }

    @Override
    public WishTargetType getTargetType() {
        return WishTargetType.PROPERTY;
    }

    @Override
    public List<PropertyResponseDTO> getTargetDTOs(List<Long> targetIds) {
        return propertyRepository.findAllById(targetIds).stream()
                .map(propertyMapper::toResponseDto)
                .toList();
    }
}
