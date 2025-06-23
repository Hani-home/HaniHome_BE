package org.hanihome.hanihomebe.wishlist.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hanihome.hanihomebe.wishlist.domain.enums.WishTargetType;

@Getter
@NoArgsConstructor
public class WishItemRequestDTO {
    private WishTargetType targetType;
    private Long targetId;
}
