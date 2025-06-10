package org.hanihome.hanihomebe.property.domain.option;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CategoryCode {
    CAT1("빌에 포함된 항목"),
    CAT2("다음 항목이 가능한지 선택해주세요"),
    CAT3("기본 제공 가전/가구"),
    CAT4("매물 장점"),
    CAT5("부동산 중개 여부"),
    ;

    private final String name;
}
