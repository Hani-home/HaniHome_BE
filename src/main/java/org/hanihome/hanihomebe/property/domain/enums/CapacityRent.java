package org.hanihome.hanihomebe.property.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CapacityRent {
    ONE("1명"),
    TWO("2명"),
    THREE("3명"),
    FOUR("4명"),
    OTHER("5명 이상")
    ;

    private final String description;
}
