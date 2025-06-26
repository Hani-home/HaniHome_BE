package org.hanihome.hanihomebe.wishlist.application.service;

import org.hanihome.hanihomebe.wishlist.domain.enums.WishTargetType;

import java.util.List;

public interface WishTargetCounter {
    WishTargetType getTargetType();
    void increment(Long memberId);
    void decrement(Long memberId);
}
