package org.hanihome.hanihomebe.temporaryProperty.web.dto.create;

import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.property.domain.enums.CapacityRent;
import org.hanihome.hanihomebe.property.domain.enums.GenderPreference;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.property.domain.enums.RealEstateType;
import org.hanihome.hanihomebe.property.domain.enums.RentPropertySubType;
import org.hanihome.hanihomebe.property.domain.vo.TimeSlot;
import org.hanihome.hanihomebe.property.domain.vo.ViewingAvailableDateTime;
import org.hanihome.hanihomebe.property.web.dto.request.create.CostDetailsDTO;
import org.hanihome.hanihomebe.property.web.dto.request.create.LivingConditionsDTO;
import org.hanihome.hanihomebe.property.web.dto.request.create.MoveInInfoDTO;
import org.hanihome.hanihomebe.property.web.dto.request.create.PropertyCreateRequestDTO;
import org.hanihome.hanihomebe.temporaryProperty.domain.vo.TemporaryCostDetails;
import org.hanihome.hanihomebe.temporaryProperty.domain.vo.TemporaryLivingConditions;
import org.hanihome.hanihomebe.temporaryProperty.domain.vo.temporaryDetails.TemporaryRentInternalDetails;

import java.time.LocalDate;
import java.util.List;

public record TemporaryRentPropertyCreateRequestDTO(
        Long id,
        PropertySuperType kind,
        GenderPreference genderPreference,          // 선호 성별
        boolean lgbtAvailable,
        Region region,                              // 주소 정보 (Embedded 타입)
        List<String> photoUrls,
        CostDetailsDTO costDetails,
        List<Long> optionItemIds,
        LivingConditionsDTO livingConditions,
        MoveInInfoDTO moveInInfo,
        LocalDate meetingDateFrom,
        LocalDate meetingDateTo,
        List<TimeSlot> timeSlots,
        List<ViewingAvailableDateTime> viewingAvailableDateTimes,
        boolean viewingAlwaysAvailable,
        String description,                         // 매물 소개
        RentPropertySubType rentPropertySubType,
        TemporaryRentInternalDetails internalDetails,
        CapacityRent capacityRent,
        RealEstateType isRealEstateIntervention
) implements TemporaryPropertyCreateRequestDTO {
    //생성자 커스터마이징해서 array들은 null 경사해서 기본값 넣어줘야 겠네

}
