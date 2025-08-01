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
    private boolean isImmediate;
    private boolean isNegotiable;
}
