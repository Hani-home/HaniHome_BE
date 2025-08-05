package org.hanihome.hanihomebe.notification.domain;

import lombok.RequiredArgsConstructor;

/// Event name
@RequiredArgsConstructor
public enum TaskType {
    VIEWING_REMINDER("뷰잉 24시간전 리마인드"),
    VIEWING_CREATED("뷰잉 생성 알림"),
    VIEWING_CANCELED("뷰잉 취소 알림"),
    ONE_ON_ONE_CONSULT_REPLIED("일대일 상담 답변 완료 알림"),
    VERIFICATION_CHECKED("신원 인증 검수 완료 알림"),
    PROPERTY_MEETING_DATE_REMINDER_BEFORE_24("매물의 뷰잉가능기간 종료 24시간 전 발송"),
    PROPERTY_MEETING_DATE_REMINDER_BEFORE_48("매물의 뷰잉가능기간 종료 48시간 전 발송"),

    ;
    private final String description;
}
