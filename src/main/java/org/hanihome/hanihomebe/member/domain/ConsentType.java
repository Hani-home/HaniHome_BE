package org.hanihome.hanihomebe.member.domain;

import lombok.Getter;

@Getter
public enum ConsentType {
    // 필수 동의 항목
    SERVICE_TERMS(true, "서비스 이용약관 동의"),
    PRIVACY_COLLECTION(true, "개인정보 수집 및 이용 동의"),
    OPERATION_POLICY(true, "운영 정책 동의"),

    // 선택 동의 항목
    LOCATION_BASED_SERVICE(false, "위치 기반 서비스 이용 동의");


    private final boolean required;
    private final String description;

    ConsentType(boolean required, String description) {
        this.required = required;
        this.description = description;
    }

    public boolean isRequired() {
        return required;
    }

    public String getDescription() {
        return description;
    }


}
