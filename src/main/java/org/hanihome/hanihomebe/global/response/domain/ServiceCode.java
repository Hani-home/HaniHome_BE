package org.hanihome.hanihomebe.global.response.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ServiceCode {

    /**
     * 성공
     * */
    SUCCESS(HttpStatus.OK,"정상 처리되었습니다."),

    /**
     * 실패
     * */
    // Member
    USERNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다"),
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다"),
    MEMBER_NOT_EXISTS(HttpStatus.BAD_REQUEST, "해당 ID의 회원이 존재하지 않습니다."),
    MEMBER_ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "이미 등록된 회원입니다"),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    // jwt
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "리프레시 토큰 검증에 실패했습니다."),
    TOKEN_MISMATCH(HttpStatus.BAD_REQUEST, "연관된 토큰이 아닙니다"),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효한 토큰이 아닙니다"),
    MALFORMED_TOKEN(HttpStatus.BAD_REQUEST, "토큰의 구조가 올바르지 않습니다"),
    UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "미지원하는 토큰입니다"),
    ACCESS_TOKEN_MISSING(HttpStatus.BAD_REQUEST, "액세스 토큰이 없습니다."),
    ACCESS_TOKEN_BLACKLISTED(HttpStatus.BAD_REQUEST, "로그아웃 처리된 액세스 토큰입니다"),
    ACCESS_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "만료된 액세스 토큰입니다."),

    // Property
    PROPERTY_NOT_EXISTS(HttpStatus.BAD_REQUEST, "요청한 매물은 존재하지 않습니다"),
    INVALID_PROPERTY_TYPE(HttpStatus.BAD_REQUEST, "해당하는 Property SuperType이 없습니다."),

    // Viewing
    VIEWING_NUMBER_NOT_SATISFIED(HttpStatus.BAD_REQUEST, "요청 가능한 뷰잉 시간대는 최소 1개 최대 3개입니다"),
    VIEWING_ALREADY_PRESCHEDULED(HttpStatus.BAD_REQUEST, "요청한 뷰잉 시간대는 모두 사용자의 뷰잉 예약 일정과 겹칩니다"),
    VIEWING_NOT_EXISTS(HttpStatus.BAD_REQUEST, "요청한 뷰잉은 존재하지 않습니다"),

    // OptionCategory
    OPTION_CATEGORY_NOT_INITIALIZED(HttpStatus.BAD_REQUEST, "해당 카테고리는 아직 초기화 되지 않았습니다"),
    OPTION_CATEGORY_NOT_EXISTS(HttpStatus.BAD_REQUEST, "요청한 카테고리는 존재하지 않습니다."),

    // OptionItem
    OPTION_ITEM_NOT_EXISTS(HttpStatus.BAD_REQUEST, "요청한 옵션 아이템은 존재하지 않습니다"),

    // ScopeType
    SCOPE_TYPE_NOT_INITIALIZED(HttpStatus.BAD_REQUEST, "해당 스코프는 아직 초기화되지 않았습니다"),


    // Notification
    NOTIFICATION_NOT_EXISTS(HttpStatus.BAD_REQUEST, "요청한 알림은 존재하지 않습니다"),

    // OneOnOneConsult
    ONE_ON_ONE_CONSULT_NOT_EXISTS(HttpStatus.BAD_REQUEST, "요청한 일대일 상담문의글은 존재하지 않습니다"),

    //S3
    S3_URI_CONVERSION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3 URI 생성 중 오류가 발생했습니다."),


    //verification
    VERIFICATION_NOT_EXISTS(HttpStatus.BAD_REQUEST, "요청한 신원 인증 요청은 존재하지 않습니다"),


    //wishItem
    ALEADY_WISH_EXISTS(HttpStatus.BAD_REQUEST, "이미 찜한 매물입니다."),
    INVALID_TARGET_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 타입입니다."),

    // not defined
    NOT_DEFINED_ERROR(HttpStatus.BAD_REQUEST, "정의 되지 않은 에러입니다"),
    NOTIFICATION_SEND_FAILED(HttpStatus.BAD_REQUEST, "알림 전송에 실패했습니다"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
