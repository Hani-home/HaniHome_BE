package org.hanihome.hanihomebe.property.web.dto.response;

import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.hanihome.hanihomebe.property.domain.*;
import org.hanihome.hanihomebe.property.domain.enums.*;
import org.hanihome.hanihomebe.property.domain.vo.CostDetails;
import org.hanihome.hanihomebe.property.domain.vo.LivingConditions;
import org.hanihome.hanihomebe.property.domain.vo.MoveInInfo;
import org.hanihome.hanihomebe.property.domain.vo.ShareInternalDetails;

import java.time.LocalDateTime;
import java.util.*;

public record SharePropertyResponseDTO(
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
        SharePropertySubType sharePropertySubType,                // 1. 매물 유형 (세컨드룸/마스터룸/거실쉐어)
        ShareInternalDetails internalDetails,                       // 2-6. 해당 매물의 층수
        CapacityShare capacityShare                             // 3. 수용 인원
//        LocalDate meetingDateFrom,
//        LocalDate meetingDateTo,
//        List<TimeSlot> timeSlots,
        // viewingAvailableDateTime는 응답에서 제외
) implements PropertyResponseDTO, PropertyDTOByView {
    public static SharePropertyResponseDTO from(ShareProperty shareProperty, List<OptionItemResponseDTO> optionItems) {
        if (shareProperty == null) {
            return null;
        }

        Long extractedMemberId = (shareProperty.getMember() != null) ? shareProperty.getMember().getId() : null;



        // photoUrls 추출 (shareProperty.getPhotoUrls()가 List<String>을 반환한다고 가정)
        // 만약 List<PhotoObject>를 반환하고 각 PhotoObject에 getUrl() 메서드가 있다면 매핑 필요
        List<String> extractedPhotoUrls = (shareProperty.getPhotoUrls() != null) ?
                new ArrayList<>(shareProperty.getPhotoUrls()) : // 방어적 복사
                Collections.emptyList();

//        List<TimeSlot> timeSlots = new ArrayList<>(shareProperty.getTimeSlots());

        return new SharePropertyResponseDTO(
                // BaseEntity 공통 필드
                shareProperty.getId(),
                shareProperty.getWishCount(),
                shareProperty.getCreatedAt(),
                shareProperty.getLastModifiedAt(),

                // 공통 프로퍼티
                extractedMemberId,
                optionItems,
                shareProperty.getDisplayStatus(),
                shareProperty.getTradeStatus(),
                shareProperty.getKind(),
                shareProperty.getGenderPreference(),
                shareProperty.isLgbtAvailable(),
                shareProperty.getRegion(),

                // 미디어 & 옵션
                extractedPhotoUrls,
                shareProperty.getThumbnailUrl(),
                shareProperty.getCostDetails(),
                shareProperty.getLivingConditions(),

                // 예약 가능 기간
                shareProperty.getMoveInInfo(),

                // 추가 속성
                shareProperty.getParkingOption(),
                shareProperty.getDescription(),

                // ShareProperty 고유 필드
                shareProperty.getSharePropertySubType(),
                shareProperty.getShareInternalDetails(),
                shareProperty.getCapacityShare()
        );
    }
}