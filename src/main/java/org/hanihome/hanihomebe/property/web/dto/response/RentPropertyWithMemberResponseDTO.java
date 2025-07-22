package org.hanihome.hanihomebe.property.web.dto.response;

import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.hanihome.hanihomebe.member.web.dto.MemberSummaryDTO;
import org.hanihome.hanihomebe.property.domain.enums.*;
import org.hanihome.hanihomebe.property.domain.vo.CostDetails;
import org.hanihome.hanihomebe.property.domain.vo.LivingConditions;
import org.hanihome.hanihomebe.property.domain.vo.MoveInInfo;
import org.hanihome.hanihomebe.property.domain.vo.RentInternalDetails;
import org.hanihome.hanihomebe.property.web.dto.response.basic.RentPropertyResponseDTO;
import org.hanihome.hanihomebe.property.web.dto.response.summary.MetaInfo;

import java.time.LocalDateTime;
import java.util.List;

public record RentPropertyWithMemberResponseDTO(
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
        CapacityRent capacityRent,             // (RentProperty 고유) 수용인원-렌트
        MemberSummaryDTO hostSummary,
        MetaInfo metaInfo
) implements PropertyWithMemberResponseDTO {
    public static RentPropertyWithMemberResponseDTO from(RentPropertyResponseDTO rentDTO,
                                                         MemberSummaryDTO hostSummary,
                                                         MetaInfo metaInfo) {
        return new RentPropertyWithMemberResponseDTO(
                rentDTO.id(),
                rentDTO.kind(),
                rentDTO.rentPropertySubType(),
                rentDTO.displayStatus(),
                rentDTO.tradeStatus(),
                rentDTO.wishCount(),
                rentDTO.createdAt(),
                rentDTO.lastModifiedAt(),
                rentDTO.memberId(),
                rentDTO.optionItems(),
                rentDTO.genderPreference(),
                rentDTO.lgbtAvailable(),
                rentDTO.region(),
                rentDTO.photoUrls(),
                rentDTO.thumbnailUrl(),
                rentDTO.costDetails(),
                rentDTO.livingConditions(),
                rentDTO.moveInInfo(),
                rentDTO.description(),
                rentDTO.internalDetails(),
                rentDTO.capacityRent(),
                hostSummary,
                metaInfo
        );
    }
}
