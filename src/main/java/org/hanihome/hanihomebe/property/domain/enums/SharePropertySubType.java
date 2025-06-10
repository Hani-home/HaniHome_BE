package org.hanihome.hanihomebe.property.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** 매물 유형 */
@Getter
@RequiredArgsConstructor
public enum SharePropertySubType {
    SECOND_ROOM("세컨드 룸")
    ,MASTER_ROOM("마스터 룸")
    ,LIVING_SHARE("거실 쉐어")
    ;

    private final String name;
}
