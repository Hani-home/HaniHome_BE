package org.hanihome.hanihomebe.property.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** 매물 유형 */
@Getter
@RequiredArgsConstructor
public enum RentPropertySubType {
    HOUSE("하우스"),
    APARTMENT("아파트"),
    UNIT("유닛"),
    STUDIO("스튜디오"),
    GRANNY_FLAT("그래니 플랫"),
    TOWN_HOUSE("타운 하우스"),
    ;

    private final String description;
}
