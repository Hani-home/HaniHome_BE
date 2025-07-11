package org.hanihome.hanihomebe.property.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RealEstateType {
    INDIVIDUAL("개인 임대"),
    REAL_ESTATE("부동산 중개")
    ;

    private final String description;
}
