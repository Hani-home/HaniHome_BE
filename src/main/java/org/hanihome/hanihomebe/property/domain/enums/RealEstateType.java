package org.hanihome.hanihomebe.property.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RealEstateType {
    INDIVIDUAL("개인 임대"),
    REAL_ESTATE("부동산 중개")
    ;
    //요건 boolean으로 안하고 enum으로 하신 이유 있나요?
    private final String description;
}
