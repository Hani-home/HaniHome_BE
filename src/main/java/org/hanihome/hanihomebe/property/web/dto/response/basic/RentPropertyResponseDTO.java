package org.hanihome.hanihomebe.property.web.dto.response.basic;
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
        PropertySuperType kind,
        RentPropertySubType rentPropertySubType,    // (RentProperty 고유) 매물 유형
        DisplayStatus displayStatus,
        TradeStatus tradeStatus,
        int wishCount,
        LocalDateTime createdAt,
        LocalDateTime lastModifiedAt,
        Long memberId,
        List<OptionItemResponseDTO> optionItems,
        GenderPreference genderPreference,
        boolean lgbtAvailable,
        Region region,
        List<String> photoUrls,
        String thumbnailUrl,
        CostDetails costDetails,
        LivingConditions livingConditions,
        MoveInInfo moveInInfo,
        String description,
        RentInternalDetails internalDetails,
        CapacityRent capacityRent                  // (RentProperty 고유) 수용인원-렌트
        //        LocalDate meetingDateFrom,
//        LocalDate meetingDateTo,
//        List<TimeSlot> timeSlots,
        // viewingAvailableDateTime는 응답에서 제외
)implements PropertyResponseDTO {
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
                // BaseEntity 공통 필드
                rentProperty.getId(),
                rentProperty.getKind(),
                rentProperty.getRentPropertySubType(),
                rentProperty.getDisplayStatus(),
                rentProperty.getTradeStatus(),
                rentProperty.getWishCount(),
                rentProperty.getCreatedAt(),
                rentProperty.getLastModifiedAt(),
                // Member 및 옵션
                memberId,
                optionItems,
                rentProperty.getGenderPreference(),
                rentProperty.isLgbtAvailable(),
                rentProperty.getRegion(),
                // 미디어 및 옵션
                photoUrls,
                rentProperty.getThumbnailUrl(),
                // 상세 정보
                rentProperty.getCostDetails(),
                rentProperty.getLivingConditions(),
                rentProperty.getMoveInInfo(),
                rentProperty.getDescription(),
                // rent 고유 정보
                rentProperty.getRentInternalDetails(),
                rentProperty.getCapacityRent()
        );
}
}