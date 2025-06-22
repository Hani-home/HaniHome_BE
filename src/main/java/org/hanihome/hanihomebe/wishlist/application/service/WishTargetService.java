package org.hanihome.hanihomebe.wishlist.application.service;

import org.hanihome.hanihomebe.wishlist.domain.enums.WishTargetType;

import java.util.List;

public interface WishTargetService {
    WishTargetType getTargetType();
    List<?> getTargetDTOs(List<Long> tergetIds);
}
