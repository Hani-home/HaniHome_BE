package org.hanihome.hanihomebe.property.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GenderPreference {
    ANY("무관"),
    MALE_ONLY("남자만"),
    FEMALE_ONLY("여자만"),
    COUPLE("커플 가능")
    ;
    private final String description;
}
