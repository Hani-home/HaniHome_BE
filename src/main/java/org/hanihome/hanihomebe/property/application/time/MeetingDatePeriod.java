package org.hanihome.hanihomebe.property.application.time;

import java.time.LocalDate;

public record MeetingDatePeriod(
        LocalDate meetingDateFrom,
        LocalDate meetingDateTo
) {
    public static MeetingDatePeriod create(LocalDate meetingDateFrom, LocalDate meetingDateTo) {
        return new MeetingDatePeriod(meetingDateFrom, meetingDateTo);
    }
}
