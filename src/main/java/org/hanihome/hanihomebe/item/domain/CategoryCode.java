package org.hanihome.hanihomebe.item.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CategoryCode {
    /// 매물 카테고리
    PROPERTY_CAT1("매물의 장점은 무엇인가요"),
    PROPERTY_CAT2("기본 제공 가전/가구를 선택해주세요"),
    PROPERTY_CAT3("다음 항목이 가능한지 선택해주세요"),
    PROPERTY_CAT4("빌에 포함된 항목"),
    PROPERTY_CAT5("부동산 중개 여부를 알려주세요"),

    /// 뷰잉 카테고리
    VIEWING_CAT1("취소 사유를 선택해주세요_게스트"),
    VIEWING_CAT2("뷰잉 체크리스트"),
    VIEWING_CAT3("취소 사유를 선택해주세요_호스트"),
    ;

    private final String name;
}