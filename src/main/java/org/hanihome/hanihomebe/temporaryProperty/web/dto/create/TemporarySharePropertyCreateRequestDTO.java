package org.hanihome.hanihomebe.temporaryProperty.web.dto.create;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.property.domain.enums.CapacityShare;
import org.hanihome.hanihomebe.property.domain.enums.GenderPreference;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.property.domain.enums.SharePropertySubType;
import org.hanihome.hanihomebe.property.domain.vo.ShareInternalDetails;
import org.hanihome.hanihomebe.property.domain.vo.TimeSlot;
import org.hanihome.hanihomebe.property.domain.vo.ViewingAvailableDateTime;
import org.hanihome.hanihomebe.property.web.dto.request.create.CostDetailsDTO;
import org.hanihome.hanihomebe.property.web.dto.request.create.LivingConditionsDTO;
import org.hanihome.hanihomebe.property.web.dto.request.create.MoveInInfoDTO;
import org.hanihome.hanihomebe.temporaryProperty.domain.vo.temporaryDetails.TemporaryShareInternalDetails;

import java.time.LocalDate;
import java.util.List;


public record TemporarySharePropertyCreateRequestDTO(
        PropertySuperType kind,                     // 매물 종류 (SHARE / RENT)
        GenderPreference genderPreference,          // 선호 성별
        boolean lgbtAvailable,
        Region region,                              // 주소 정보 (Embedded 타입)
        List<String> photoUrls,                     // 매물 사진 URL 리스트
        CostDetailsDTO costDetails,
        List<Long> optionItemIds,                   // 포함된 비용 항목 리스트
        LivingConditionsDTO livingConditions,
        MoveInInfoDTO moveInInfo,
        LocalDate meetingDateFrom,
        LocalDate meetingDateTo,
        List<TimeSlot> timeSlots,
        List<ViewingAvailableDateTime> viewingAvailableDateTimes,
        boolean viewingAlwaysAvailable,
        String description,                         // 매물 소개
        SharePropertySubType sharePropertySubType,  //고유필드 1. 매물 유형 (세컨드룸/마스터룸/거실쉐어)
        TemporaryShareInternalDetails internalDetails,
        CapacityShare capacityShare
) implements TemporaryPropertyCreateRequestDTO {
    //이것도 생성자 커스터마지징 => null 검사 후 기본값
}
