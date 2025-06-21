package org.hanihome.hanihomebe.property.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CapacityShare {
    SINGLE("독방"),
    DOUBLE("2인 1실"),
    TRIPLE("3인 1실"),
    OTHER("n인 1실")
    ;

    private final String value;
}
