package org.hanihome.hanihomebe.admin.customerservice.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OneOnOneConsultStatus {
    REQUESTED("답변 미완"),
    COMPLETED("답변 완료"),
    ;

    private final String description;
}
