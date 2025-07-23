package org.hanihome.hanihomebe.temporaryProperty.web.dto;

import org.hanihome.hanihomebe.property.domain.vo.CostDetails;
import org.hanihome.hanihomebe.property.domain.vo.TimeSlot;

import java.time.LocalDate;
import java.util.List;

//빌에 포함된 항목도 받아야함
public record ContractDTO(
        CostDetails costDetails,
        List<Long> IncludedOptionItemIds, //빌에포함된 친구들도 OptionItem에 있음
        LocalDate meetingDateFrom,
        LocalDate meetingDateTo,
        List<TimeSlot> timeSlots,
        boolean viewingAlwaysAvailable
) {

}
