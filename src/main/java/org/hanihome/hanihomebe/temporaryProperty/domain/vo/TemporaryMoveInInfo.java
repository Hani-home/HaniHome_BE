package org.hanihome.hanihomebe.temporaryProperty.domain.vo;

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
public class TemporaryMoveInInfo {
    private LocalDateTime availableFrom;
    private LocalDateTime availableTo;
    private Boolean isImmediate;
    private Boolean isNegotiable;

    public static TemporaryMoveInInfo empty() {
        return new TemporaryMoveInInfo().builder()
                .availableFrom(null)
                .availableTo(null)
                .isImmediate(null)
                .isNegotiable(null)
                .build();
    }
}
