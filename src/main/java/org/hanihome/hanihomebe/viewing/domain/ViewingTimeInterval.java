package org.hanihome.hanihomebe.viewing.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum ViewingTimeInterval {
    MINUTE30(30),
    ;

    private final long number;
}
