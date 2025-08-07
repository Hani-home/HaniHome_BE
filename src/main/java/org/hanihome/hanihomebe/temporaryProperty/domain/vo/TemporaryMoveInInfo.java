package org.hanihome.hanihomebe.temporaryProperty.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("immediate")
    private Boolean isImmediate;
    @JsonProperty("negotiable")
    private Boolean isNegotiable;

    public static TemporaryMoveInInfo empty() {
        return TemporaryMoveInInfo.builder()
                .availableFrom(null)
                .availableTo(null)
                .isImmediate(null)
                .isNegotiable(null)
                .build();
    }
}
