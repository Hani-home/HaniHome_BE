package org.hanihome.hanihomebe.property.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class TimeSlot {
    private LocalTime timeFrom; // 30분 단위:
    private LocalTime timeTo;   // 00:00, 00:30, 01:00, ...
}
