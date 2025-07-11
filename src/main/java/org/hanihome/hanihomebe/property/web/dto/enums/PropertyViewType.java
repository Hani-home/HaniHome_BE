package org.hanihome.hanihomebe.property.web.dto.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PropertyViewType {
    DEFAULT("상세정보"),
    SUMMARY("매물 카드 형식에서 제공되는 정보"),
    ;
    private final String description;
}
