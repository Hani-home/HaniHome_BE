package org.hanihome.hanihomebe.item.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScopeCode {
    SCOPE_RENT("RENT","렌트 매물 페이지"),
    SCOPE_SHARE("SHARE","쉐어 매물 페이지"),
    SCOPE_VIEWING("VIEWING","뷰잉 관리 페이지"),
    ;
    private final String code;
    private final String description;
}
