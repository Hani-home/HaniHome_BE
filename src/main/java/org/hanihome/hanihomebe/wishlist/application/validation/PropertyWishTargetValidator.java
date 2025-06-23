package org.hanihome.hanihomebe.wishlist.application.validation;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.property.repository.PropertyRepository;
import org.hanihome.hanihomebe.wishlist.domain.enums.WishTargetType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PropertyWishTargetValidator implements WishTargetValidator {

    private final PropertyRepository propertyRepository;

    @Override
    public WishTargetType getType() {
        return WishTargetType.PROPERTY;
    }

    @Override
    public void validate(Long targetId) {
        if(!propertyRepository.existsById(targetId)) {
            throw new CustomException(ServiceCode.PROPERTY_NOT_EXISTS);
        }
    }


}
