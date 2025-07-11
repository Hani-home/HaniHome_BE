package org.hanihome.hanihomebe.property.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Embeddable
public class MoveInInfo {
    // 입주 정보
    private LocalDateTime availableFrom;
    private LocalDateTime availableTo;
    private boolean isImmediate;
    private boolean isNegotiable;

}
