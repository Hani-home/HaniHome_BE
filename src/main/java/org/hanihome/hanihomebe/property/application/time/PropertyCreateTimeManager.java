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
import java.util.List;

public class PropertyCreateTimeManager {
    public static List<ViewingAvailableDateTime> validateAndGenerateViewingAvailableDateTimes(List<TimeSlot> timeSlots,
                                                                                              MeetingDatePeriod meetingDatePeriod) {
        // 뷰잉 가능 시간 검증
        validateTimeSlots(timeSlots);

        // ViewingAvailableDateTime 변환
        List<ViewingAvailableDateTime> generated = generateViewingAvailableDateTimes(timeSlots, meetingDatePeriod);
        return generated;
    }


    public static List<ViewingAvailableDateTime> validateAndGenerateTowMonthsOfViewingAvailableDateTimes(List<TimeSlot> timeSlots) {
        validateTimeSlots(timeSlots);

        MeetingDatePeriod meetingDatePeriod = buildTwoMonths();

        return generateViewingAvailableDateTimes(timeSlots, meetingDatePeriod);
    }

    private static void validateTimeSlots(List<TimeSlot> timeSlots) {
        boolean isValidTimeSlots = TimeSlotValidator.validateAllConditions(timeSlots);
        if(!isValidTimeSlots) {
            throw new CustomException(ServiceCode.INVALID_PROPERTY_TIME_SLOT);
        }
    }

    private static MeetingDatePeriod buildTwoMonths() {
        LocalDate meetingDateFrom = LocalDateTime
                .now(ZoneId.of("UTC")).
                toLocalDate() ;
        LocalDate meetingDateTo = meetingDateFrom.plusMonths(2);
        MeetingDatePeriod meetingDatePeriod = MeetingDatePeriod.create(meetingDateFrom, meetingDateTo);
        return meetingDatePeriod;
    }

    private static List<ViewingAvailableDateTime> generateViewingAvailableDateTimes(List<TimeSlot> timeSlots, MeetingDatePeriod meetingDatePeriod) {
        LocalDate meetingDateFrom = meetingDatePeriod.meetingDateFrom();
        LocalDate meetingDateTo = meetingDatePeriod.meetingDateTo();

        return ViewingAvailableDateTimeGenerator.generate(meetingDateFrom, meetingDateTo, timeSlots);
    }
}
