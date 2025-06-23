package org.hanihome.hanihomebe.wishlist.application.validation;

import org.hanihome.hanihomebe.wishlist.domain.enums.WishTargetType;

public interface WishTargetValidator {
    WishTargetType getType();
    void validate(Long targetId); //해당 ID가 실제 존재하는지 검증
}
