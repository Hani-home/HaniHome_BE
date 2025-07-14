package org.hanihome.hanihomebe.property.application;

import org.hanihome.hanihomebe.property.domain.vo.TimeSlot;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TimeSlotValidator {
    /**
     * TimeSlot 검증: 개수가 1이상 3이하 && 시간이 30분 단위
     * @param timeSlots 검증할 TimeSlot 리스트
     * @return 검증 결과
     */
    public static boolean validAllConditions(List<TimeSlot> timeSlots) {
        if (!(timeSlots.size() >= 1 && timeSlots.size() <= 3)) {
            return false;
        }

        boolean isUnitTimes = timeSlots.stream()
                .anyMatch(slot -> {
                    int fromMin = slot.getTimeFrom().getMinute();
                    int toMin = slot.getTimeTo().getMinute();

                    return (fromMin % 30 != 0 || toMin % 30 != 0) ? false : true;
                });

        return isUnitTimes;
    }
    /**
     * TimeSlot 검증: 시간이 30분 단위
     * @param timeSlots 검증할 TimeSlot 리스트
     * @return 검증 결과
     */
    public static boolean isDividedBy30(List<TimeSlot> timeSlots) {
        return timeSlots.stream()
                .anyMatch(slot -> {
                    int fromMin = slot.getTimeFrom().getMinute();
                    int toMin = slot.getTimeTo().getMinute();

                    return (fromMin % 30 != 0 || toMin % 30 != 0) ? false : true;
                });
    }



}
