package org.hanihome.hanihomebe.notification.domain;

import lombok.RequiredArgsConstructor;

/// Event name
@RequiredArgsConstructor
public enum NotificationType {
    VIEWING_REMINDER("뷰잉 24시간전 리마인드"),
    VIEWING_CREATED("뷰잉 생성 알림"),
    VIEWING_CANCELED("뷰잉 취소 알림"),
    ONE_ON_ONE_CONSULT_REPLIED("일대일 상담 답변 완료 알림"),
    VERIFICATION_CHECKED("신원 인증 검수 완료 알림"),
    ;
    private final String description;
}
