package org.hanihome.hanihomebe.item.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CategoryCode {
    /// 매물 카테고리
    PROPERTY_CAT1("빌에 포함된 항목"),
    PROPERTY_CAT2("다음 항목이 가능한지 선택해주세요"),
    PROPERTY_CAT3("기본 제공 가전/가구"),
    PROPERTY_CAT4("매물 장점"),
    PROPERTY_CAT5("부동산 중개 여부를 알려주세요"),

    /// 뷰잉 카테고리
    VIEWING_CAT1("취소 사유를 선택해주세요"),
    VIEWING_CAT2("뷰잉 체크리스트")
    ;

    private final String name;
}