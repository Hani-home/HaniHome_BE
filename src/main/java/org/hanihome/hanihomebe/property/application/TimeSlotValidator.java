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
    public static boolean validateAllConditions(List<TimeSlot> timeSlots) {
        if (isValidSize(timeSlots) && isDividedBy30(timeSlots)) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isValidSize(List<TimeSlot> timeSlots) {
        return timeSlots.size() >= 1 && timeSlots.size() <= 3;
    }

    /**
     * TimeSlot 검증: 시간이 30분 단위
     * @param timeSlots 검증할 TimeSlot 리스트
     * @return 검증 결과
     */
    private static boolean isDividedBy30(List<TimeSlot> timeSlots) {
        return timeSlots.stream()
                .allMatch(slot -> {
                    int fromMin = slot.getTimeFrom().getMinute();
                    int toMin = slot.getTimeTo().getMinute();

                    return (fromMin % 30 != 0 || toMin % 30 != 0) ? false : true;
                });
    }
}
