package org.hanihome.hanihomebe.property.web.dto.request;

import lombok.extern.slf4j.Slf4j;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.interest.region.Region;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.hanihome.hanihomebe.property.application.TimeSlotValidator;
import org.hanihome.hanihomebe.property.domain.enums.*;
import org.hanihome.hanihomebe.property.domain.vo.*;
import org.hanihome.hanihomebe.viewing.domain.ViewingTimeInterval;

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
        CostDetails costDetails,
        List<Long> optionItemIds,       // 포함된 비용 항목 리스트
        LivingConditions livingConditions,
        MoveInInfo moveInInfo,
        ParkingOption parkingOption,                // 주차 옵션
//        Set<LocalDateTime> possibleMeetingDates,            // 뷰잉 가능 날짜 집합
        LocalDate meetingDateFrom,
        LocalDate meetingDateTo,
        List<TimeSlot> timeSlots,
        List<ViewingAvailableDateTime> viewingAvailableDateTimes,
        boolean viewingAlwaysAvailable,
        String description,                         // 매물 소개
        RentPropertySubType rentPropertySubType,    // (RentProperty 고유) 매물 유형
        RealEstateType isRealEstateIntervention,    // (RentProperty 고유) 부동산 중개 여부
        RentInternalDetails internalDetails,
        CapacityRent capacityRent,                  // (RentProperty 고유) 수용인원-렌트
        Exposure exposure                           // (RentProperty 고유) 남향북향
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
            BigDecimal latitude = region.getLatitude();
            BigDecimal longitude = region.getLongitude();
            if (isValidLatitudeAndLongitude(latitude, longitude)) {
                throw new CustomException(ServiceCode.INVALID_LATITUDE_LONGITUDE);
            }
        }
        // 뷰잉 가능 시간 검증
        boolean isValidTimeSlots = TimeSlotValidator.validAllConditions(timeSlots);
        if(!isValidTimeSlots) {
            throw new CustomException(ServiceCode.INVALID_PROPERTY_TIME_SLOT);
        }

        // ViewingAvailableDateTime 변환
        LocalDate tempDate = meetingDateFrom;
        while (tempDate.isBefore(meetingDateTo) || tempDate.isEqual(meetingDateTo)) {
            log.info("현재 DTO 생성중의 date:{}", tempDate.toString() );
            LocalDate finalTempDate = tempDate;
            List<ViewingAvailableDateTime> finalViewingAvailableDateTimes = viewingAvailableDateTimes;
            timeSlots.forEach(timeSlot -> {
                LocalTime timeFrom = timeSlot.getTimeFrom();
                LocalTime timeTo = timeSlot.getTimeTo();
                while(timeFrom.isBefore(timeTo)) {
                    ViewingAvailableDateTime viewingAvailableDateTime = new ViewingAvailableDateTime(finalTempDate,
                            timeFrom,
                            false,
                            ViewingTimeInterval.MINUTE30);
                    finalViewingAvailableDateTimes.add(viewingAvailableDateTime);
                    timeFrom = timeFrom.plusMinutes(30);
                }
            });
            tempDate = tempDate.plusDays(1);
        }
        log.info("meetingDateFrom: {}, meetingDateTo: {}", meetingDateFrom, meetingDateTo);
        log.info("viewingAvailableDateTimes: {}", viewingAvailableDateTimes.stream().map(viewingAvailableDateTime -> viewingAvailableDateTime.getTime()).toList());
    }


}
