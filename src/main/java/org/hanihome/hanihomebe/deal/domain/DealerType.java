package org.hanihome.hanihomebe.deal.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DealerType {
    DEAL_AS_GUEST("게스트로서 거래"),
    DEAL_AS_HOST("호스트로서 거래"),
    ;
    private final String description;
}
