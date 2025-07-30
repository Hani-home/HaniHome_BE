package org.hanihome.hanihomebe.notification.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NotificationSendStatus {
    PENDING("전송 대기 중, 조회 불가능"),
    SUCCESS("sse 전송에 서버측 문제는 없음, 조회 가능"), // sse 전송 후 실제 수신까지 성공 or sse 전송 시도에는 오류가 없으나 클라이언트가 sse 미연결이라 전송은 안됐음 but 문제 없음
    FAILED("sse 전송 실패, 조회 가능"),
    CANCELLED("전송 취소, 조회 불가능"),
    ;
    private final String description;
}
