package org.hanihome.hanihomebe.wishlist.application.service;

import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.repository.PropertyRepository;
import org.hanihome.hanihomebe.wishlist.domain.enums.WishTargetType;
import org.springframework.stereotype.Component;

@Component
public class PropertyWishCounter implements WishTargetCounter {

    private final PropertyRepository propertyRepository;

    public PropertyWishCounter(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Override
    public WishTargetType getTargetType() {
        return WishTargetType.PROPERTY;
    }

    @Override
    public void increment(Long targetId) {
        Property property = propertyRepository.findById(targetId)
                .orElseThrow(() -> new CustomException(ServiceCode.PROPERTY_NOT_EXISTS));
        property.incrementWishCount();
    }

    @Override
    public void decrement(Long targetId) {
        Property property = propertyRepository.findById(targetId)
                .orElseThrow(() -> new CustomException(ServiceCode.PROPERTY_NOT_EXISTS));
        property.decrementWishCount();
    }
}
