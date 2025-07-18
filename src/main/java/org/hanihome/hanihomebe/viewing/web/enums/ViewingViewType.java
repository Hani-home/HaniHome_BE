package org.hanihome.hanihomebe.viewing.web.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ViewingViewType {
    DEFAULT("상세 정보"),
    DATE_PROFILE("날짜, 뷰잉 참여자 프로필 이미지"),
    DATE_WITH_PROPERTY("날짜만"),
    ;

    private final String description;
}
