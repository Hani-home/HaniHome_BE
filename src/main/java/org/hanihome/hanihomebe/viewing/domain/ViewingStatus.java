package org.hanihome.hanihomebe.viewing.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ViewingStatus {
    REQUESTED("예약확정"),
    CANCELLED("취소"),
    COMPLETED("완료")
    ;

    private final String value;
}
