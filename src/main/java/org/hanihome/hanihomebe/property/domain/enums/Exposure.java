package org.hanihome.hanihomebe.property.domain.enums;

import lombok.RequiredArgsConstructor;

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
