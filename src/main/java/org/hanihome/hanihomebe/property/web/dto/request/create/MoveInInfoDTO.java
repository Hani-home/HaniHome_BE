package org.hanihome.hanihomebe.property.web.dto.request.create;

import org.hanihome.hanihomebe.property.domain.vo.MoveInInfo;
import org.hanihome.hanihomebe.temporaryProperty.domain.vo.TemporaryMoveInInfo;

import java.time.LocalDateTime;

public record MoveInInfoDTO(
         LocalDateTime availableFrom,
         LocalDateTime availableTo,
         boolean immediate,
         boolean negotiable
) {
    public MoveInInfo toVO() {
        return new MoveInInfo(
                availableFrom,
                availableTo,
                immediate,
                negotiable
        );
    }

    public TemporaryMoveInInfo toTemporaryVO() {
        return new TemporaryMoveInInfo(
                availableFrom,
                availableTo,
                immediate,
                negotiable
        );
    }
}
