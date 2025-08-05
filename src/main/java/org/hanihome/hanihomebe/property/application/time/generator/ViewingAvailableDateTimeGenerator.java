package org.hanihome.hanihomebe.property.application.time.generator;

import lombok.extern.slf4j.Slf4j;
import org.hanihome.hanihomebe.property.domain.vo.TimeSlot;
import org.hanihome.hanihomebe.property.domain.vo.ViewingAvailableDateTime;
import org.hanihome.hanihomebe.viewing.domain.ViewingTimeInterval;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ViewingAvailableDateTimeGenerator {
    public static List<ViewingAvailableDateTime> generate(LocalDate meetingDateFrom, LocalDate meetingDateTo, List<TimeSlot> timeSlots) {
        LocalDate pos = meetingDateFrom;
        List<ViewingAvailableDateTime> viewingAvailableDateTimes = new ArrayList<>();
        while (pos.isBefore(meetingDateTo) || pos.isEqual(meetingDateTo)) {
            log.info("현재 DTO 생성중의 date:{}", pos.toString() );
            LocalDate finalTempDate = pos;
            timeSlots.forEach(timeSlot -> {
                LocalTime timeFrom = timeSlot.getTimeFrom();
                LocalDateTime start = LocalDateTime.of(finalTempDate, timeFrom);
                LocalDateTime end = getEndTime(timeSlot, timeFrom, finalTempDate);

                while(start.isBefore(end)) {
                    ViewingAvailableDateTime viewingAvailableDateTime = new ViewingAvailableDateTime(start.toLocalDate(),
                            start.toLocalTime(),
                            false,
                            ViewingTimeInterval.MINUTE30);
                    viewingAvailableDateTimes.add(viewingAvailableDateTime);
                    start = start.plusMinutes(30);
                }
            });
            pos = pos.plusDays(1);
        }
        log.info("meetingDateFrom: {}, meetingDateTo: {}", meetingDateFrom, meetingDateTo);
        log.info("viewingAvailableDateTimes: {}", viewingAvailableDateTimes.stream().map(viewingAvailableDateTime -> viewingAvailableDateTime.getTime()).toList());

        return viewingAvailableDateTimes;
    }

    private static LocalDateTime getEndTime(TimeSlot timeSlot, LocalTime timeFrom, LocalDate finalTempDate) {
        LocalTime timeTo = timeSlot.getTimeTo();
        LocalDateTime end;
        if (timeTo.equals(LocalTime.MIDNIGHT) && timeFrom.isAfter(timeTo)) {
            end = LocalDateTime.of(finalTempDate.plusDays(1), LocalTime.MIDNIGHT);
        } else {
            end = LocalDateTime.of(finalTempDate, timeTo);
        }
        return end;
    }
}
