package org.hanihome.hanihomebe.wishlist.web.dto;

import lombok.Getter;
import org.hanihome.hanihomebe.wishlist.domain.WishItem;
import org.hanihome.hanihomebe.wishlist.domain.enums.WishTargetType;

@Getter
public class WishItemDTO {
    private Long memberId;
    private WishTargetType targetType;
    private Long targetId;

    public WishItemDTO(Long memberId, WishTargetType targetType, Long targetId) {
        this.memberId = memberId;
        this.targetType = targetType;
        this.targetId = targetId;
    }

    public static WishItemDTO create(WishItem wishItem) {
        return new WishItemDTO(
                wishItem.getMember().getId(),
                wishItem.getTargetType(),
                wishItem.getTargetId()
        );
    }
}
