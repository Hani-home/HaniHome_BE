package org.hanihome.hanihomebe.member.domain;

public enum ConsentType {
    // 필수 동의 항목
    SERVICE_TERMS(true, "하니홈 서비스 이용약관 동의"),
    PRIVACY_COLLECTION(true, "개인정보 수집 및 이용 동의"),
    THIRD_PARTY_SHARING(true, "개인정보 제3자 제공 동의"),
    LOCATION_BASED_SERVICE(true, "위치 기반 서비스 이용 동의"),

    // 선택 동의 항목
    MARKETING_INFO(false, "마케팅 정보 수신 동의(앱 푸시/이메일/문자)"),
    ADVERTISEMENT_AGREEMENT(false, "광고성 정보 수신 및 맞춤형 광고 노출 동의"),
    COMMUNITY_USAGE(false, "커뮤니티 콘텐츠 활용 동의"),
    POINT_REWARD_POLICY(false, "포인트 및 리워드 운영 정책 동의");

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
