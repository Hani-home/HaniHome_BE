package org.hanihome.hanihomebe.property.web.dto.request.create;

import org.hanihome.hanihomebe.property.domain.vo.TimeSlot;
import org.hanihome.hanihomebe.property.domain.vo.ViewingAvailableDateTime;

import java.time.LocalDate;
import java.util.List;

public record ProcessedViewingDatesDTO(
        LocalDate processedMeetingDateFrom,
        LocalDate processedMeetingDateTo,
        List<TimeSlot> processedTimeSlots,
        List<ViewingAvailableDateTime> viewingAvailableDateTimes
) { }
