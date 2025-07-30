package org.hanihome.hanihomebe.property.web.dto.request.create;

import lombok.extern.slf4j.Slf4j;
import org.hanihome.hanihomebe.interest.region.Region;

import java.time.LocalDate;
import java.util.List;

import org.hanihome.hanihomebe.property.application.time.MeetingDatePeriod;
import org.hanihome.hanihomebe.property.application.time.PropertyCreateTimeManager;
import org.hanihome.hanihomebe.property.domain.enums.*;
import org.hanihome.hanihomebe.property.domain.vo.*;

import java.util.ArrayList;


/**
 * RentProperty를 생성할 때 필요한 모든 속성을 담는 DTO(record).
 * 컬렉션 필드가 null로 넘어올 경우, 생성자에서 빈 컬렉션으로 초기화하도록 처리합니다.
 */
@Slf4j
public record RentPropertyCreateRequestDTO(
        Long memberId,                              // 소유자 회원 ID
        PropertySuperType kind,                     // 매물 종류 (SHARE / RENT)
        GenderPreference genderPreference,          // 선호 성별
        boolean lgbtAvailable,
        Region region,                              // 주소 정보 (Embedded 타입)
        List<String> photoUrls,                     // 매물 사진 URL 리스트
        CostDetailsDTO costDetails,
        List<Long> optionItemIds,       // 포함된 비용 항목 리스트
        LivingConditionsDTO livingConditions,
        MoveInInfoDTO moveInInfo,
        LocalDate meetingDateFrom,
        LocalDate meetingDateTo,
        List<TimeSlot> timeSlots,
        List<ViewingAvailableDateTime> viewingAvailableDateTimes,
        boolean viewingAlwaysAvailable,
        String description,                         // 매물 소개
        RentPropertySubType rentPropertySubType,    // (RentProperty 고유) 매물 유형
        RentInternalDetails internalDetails,
        CapacityRent capacityRent                  // (RentProperty 고유) 수용인원-렌트
)  implements PropertyCreateRequestDTO {
    public RentPropertyCreateRequestDTO {
        if (photoUrls == null) {
            photoUrls = new ArrayList<>();
        }
        if (optionItemIds == null) {
            optionItemIds = new ArrayList<>();
        }
        if(viewingAvailableDateTimes == null) {
            viewingAvailableDateTimes = new ArrayList<>();
        }
        // 위도, 경도
        if (region.getLatitude() != null && region.getLongitude() != null) {
            validateLatitudeAndLongitude(region.getLatitude(), region.getLongitude());
        }

        // 타임슬롯 검증, 뷰잉 가능 시간 생성
        if (viewingAlwaysAvailable) {
            viewingAvailableDateTimes = PropertyCreateTimeManager.validateAndGenerateTowMonthsOfViewingAvailableDateTimes(timeSlots);
        } else {
            MeetingDatePeriod meetingDatePeriod = MeetingDatePeriod.create(meetingDateFrom, meetingDateTo);
            viewingAvailableDateTimes = PropertyCreateTimeManager.validateAndGenerateViewingAvailableDateTimes(timeSlots, meetingDatePeriod);
        }
    }
}
