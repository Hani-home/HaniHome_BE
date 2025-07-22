package org.hanihome.hanihomebe.property.domain.enums;

import lombok.RequiredArgsConstructor;

//이거 피그마 상 UI에 있는 친구인가용?
@RequiredArgsConstructor
public enum Exposure {
    SOUTHERN("남향"),
    NORTHERN("북향"),
    EASTERN("동향"),
    WESTERN("서향"),
    NORTHEASTERN("북동향"),
    NORTHWESTERN("북서향")
    ;
    private final String description;
}
