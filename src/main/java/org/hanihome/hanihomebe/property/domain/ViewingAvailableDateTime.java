package org.hanihome.hanihomebe.property.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hanihome.hanihomebe.viewing.domain.ViewingTimeInterval;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ViewingAvailableDateTime {
    // 뷰잉 가능 날짜
    private LocalDate date;

    // 뷰잉 가능 시각
    private LocalTime time;

    // 뷰잉 예약 여부
    private boolean isReserved;

    @Enumerated(EnumType.STRING)
    private ViewingTimeInterval timeInterval = ViewingTimeInterval.MINUTE30;

    public void updateReservation(boolean isReserved) {
        this.isReserved = isReserved;
    }
}
