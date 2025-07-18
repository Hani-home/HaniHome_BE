package org.hanihome.hanihomebe.property.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DisplayStatus {
    ACTIVE("노출 상태"), 
    INACTIVE("숨김 상태"), 
    ;
    private final String description;
}
