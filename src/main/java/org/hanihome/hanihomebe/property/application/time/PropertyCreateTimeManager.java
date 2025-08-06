package org.hanihome.hanihomebe.property.application.time;

import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.property.application.time.generator.ViewingAvailableDateTimeGenerator;
import org.hanihome.hanihomebe.property.application.time.validator.TimeSlotValidator;
import org.hanihome.hanihomebe.property.domain.vo.TimeSlot;
import org.hanihome.hanihomebe.property.domain.vo.ViewingAvailableDateTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PropertyCreateTimeManager {
    public static List<TimeSlot> preprocessTimeSlots(List<TimeSlot> timeSlots) {
        if (timeSlots == null) {
            return Collections.emptyList();
        }

        return timeSlots.stream()
                .filter(timeSlot -> !(timeSlot.getTimeFrom() == null && timeSlot.getTimeTo() == null))
                .peek(timeSlot -> {
                    if (timeSlot.getTimeFrom() == null || timeSlot.getTimeTo() == null) {
                        throw new CustomException(ServiceCode.INVALID_PROPERTY_TIME_SLOT);
                    }
                })
                .collect(Collectors.toList());
    }

    public static List<ViewingAvailableDateTime> validateAndGenerateViewingAvailableDateTimes(List<TimeSlot> timeSlots,
                                                                                              MeetingDatePeriod meetingDatePeriod) {
        validateTimeSlots(timeSlots);
        List<ViewingAvailableDateTime> generated = generateViewingAvailableDateTimes(timeSlots, meetingDatePeriod);
        return generated;
    }

    public static MeetingDatePeriod buildTwoMonths() {
        LocalDate meetingDateFrom = LocalDateTime
                .now(ZoneId.of("UTC")).
                toLocalDate() ;
        LocalDate meetingDateTo = meetingDateFrom.plusMonths(2);
        MeetingDatePeriod meetingDatePeriod = MeetingDatePeriod.create(meetingDateFrom, meetingDateTo);
        return meetingDatePeriod;
    }

    private static void validateTimeSlots(List<TimeSlot> timeSlots) {
        boolean isValidTimeSlots = TimeSlotValidator.validateAllConditions(timeSlots);
        if(!isValidTimeSlots) {
            throw new CustomException(ServiceCode.INVALID_PROPERTY_TIME_SLOT);
        }
    }

    private static List<ViewingAvailableDateTime> generateViewingAvailableDateTimes(List<TimeSlot> timeSlots, MeetingDatePeriod meetingDatePeriod) {
        LocalDate meetingDateFrom = meetingDatePeriod.meetingDateFrom();
        LocalDate meetingDateTo = meetingDatePeriod.meetingDateTo();

        return ViewingAvailableDateTimeGenerator.generate(meetingDateFrom, meetingDateTo, timeSlots);
    }
}
