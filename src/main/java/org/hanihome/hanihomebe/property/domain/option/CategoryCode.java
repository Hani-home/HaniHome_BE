package org.hanihome.hanihomebe.property.domain.option;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CategoryCode {
    CAT1("빌에 포함된 항목"),
    CAT2("기타 가능/불가능 여부"),
    CAT3("기본 제공 가전/가구"),
    CAT4("매물 장점");

    private final String name;
}
