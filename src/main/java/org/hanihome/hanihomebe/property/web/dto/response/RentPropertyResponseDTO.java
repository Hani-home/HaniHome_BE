package org.hanihome.hanihomebe.property.web.dto.response;
import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.hanihome.hanihomebe.property.domain.*;
import org.hanihome.hanihomebe.property.domain.enums.*;
import org.hanihome.hanihomebe.property.domain.vo.CostDetails;
import org.hanihome.hanihomebe.property.domain.vo.LivingConditions;
import org.hanihome.hanihomebe.property.domain.vo.MoveInInfo;
import org.hanihome.hanihomebe.property.domain.vo.RentInternalDetails;

import java.time.LocalDateTime;
import java.util.*;

public record RentPropertyResponseDTO(
        Long id,
        int wishCount,
        LocalDateTime createdAt,
        LocalDateTime lastModifiedAt,
        Long memberId,
        List<OptionItemResponseDTO> optionItems,
        DisplayStatus displayStatus,
        TradeStatus tradeStatus,
        PropertySuperType kind,
        GenderPreference genderPreference,
        boolean lgbtAvailable,
        Region region,
        List<String> photoUrls,
        String thumbnailUrl,
        CostDetails costDetails,
        LivingConditions livingConditions,
        MoveInInfo moveInInfo,
        ParkingOption parkingOption,
        String description,
        RentPropertySubType rentPropertySubType,    // (RentProperty 고유) 매물 유형
        RealEstateType isRealEstateIntervention,    // (RentProperty 고유) 부동산 중개 여부
        RentInternalDetails internalDetails,
        CapacityRent capacityRent,                  // (RentProperty 고유) 수용인원-렌트
        Exposure exposure                           // (RentProperty 고유) 남향북향
        //        LocalDate meetingDateFrom,
//        LocalDate meetingDateTo,
//        List<TimeSlot> timeSlots,
        // viewingAvailableDateTime는 응답에서 제외
)implements PropertyResponseDTO, PropertyDTOByView {
    public static RentPropertyResponseDTO from(RentProperty rentProperty, List<OptionItemResponseDTO> optionItems) {

    // memberId 추출 (rentProperty.getMember()가 Member 객체를 반환하고, Member 객체에 getId()가 있다고 가정)
    Long memberId = (rentProperty.getMember() != null) ? rentProperty.getMember().getId() : null;

        // photoUrls 추출 (rentProperty.getPhotoUrls()가 List<String>을 반환한다고 가정)
    List<String> photoUrls = (rentProperty.getPhotoUrls() != null) ?
            new ArrayList<>(rentProperty.getPhotoUrls()) :
            Collections.emptyList();

          // possibleMeetingDates 추출 (방어적 복사)
//        List<TimeSlot> timeSlots = new ArrayList<>(rentProperty.getTimeSlots());


        return new RentPropertyResponseDTO(
                // BaseEntity 필드
                rentProperty.getId(),
                rentProperty.getWishCount(),
                rentProperty.getCreatedAt(),
                rentProperty.getLastModifiedAt(),

                // 공통 필드
                memberId,
                optionItems,
                rentProperty.getDisplayStatus(),
                rentProperty.getTradeStatus(),
                rentProperty.getKind(),
                rentProperty.getGenderPreference(),
                rentProperty.isLgbtAvailable(),
                rentProperty.getRegion(),

                // 미디어 및 옵션
                photoUrls,
                rentProperty.getThumbnailUrl(),
                rentProperty.getCostDetails(),
                rentProperty.getLivingConditions(),

                // 예약 가능 기간
                rentProperty.getMoveInInfo(),

                // 추가 속성
                rentProperty.getParkingOption(),
                rentProperty.getDescription(),

                // RentProperty 고유 필드
                rentProperty.getRentPropertySubType(),
                rentProperty.getIsRealEstateIntervention(),
                rentProperty.getRentInternalDetails(),
                rentProperty.getCapacityRent(),
                rentProperty.getExposure()
        );
}
}