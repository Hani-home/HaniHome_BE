package org.hanihome.hanihomebe.property.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TradeStatus {
    BEFORE("거래 전 또는 거래 가능"),
    IN_PROGRESS("거래 중"),
    COMPLETED("거래 완료"),
    ;
    private final String description;
}
