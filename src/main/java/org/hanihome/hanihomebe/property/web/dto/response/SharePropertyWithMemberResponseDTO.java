package org.hanihome.hanihomebe.property.web.dto.response;

import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.hanihome.hanihomebe.member.web.dto.MemberSummaryDTO;
import org.hanihome.hanihomebe.property.domain.enums.*;
import org.hanihome.hanihomebe.property.domain.vo.CostDetails;
import org.hanihome.hanihomebe.property.domain.vo.LivingConditions;
import org.hanihome.hanihomebe.property.domain.vo.MoveInInfo;
import org.hanihome.hanihomebe.property.domain.vo.ShareInternalDetails;
import org.hanihome.hanihomebe.property.web.dto.response.basic.SharePropertyResponseDTO;
import org.hanihome.hanihomebe.property.web.dto.response.summary.MetaInfo;

import java.time.LocalDateTime;
import java.util.List;

public record SharePropertyWithMemberResponseDTO(
        Long id,
        PropertySuperType kind,
        SharePropertySubType sharePropertySubType,                // 1. 매물 유형 (세컨드룸/마스터룸/거실쉐어)
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
        ShareInternalDetails internalDetails,                       // 2-6. 해당 매물의 층수
        CapacityShare capacityShare,                             // 3. 수용 인원
        MemberSummaryDTO hostSummary,
        MetaInfo metaInfo
) implements PropertyWithMemberResponseDTO {
    public static SharePropertyWithMemberResponseDTO from(SharePropertyResponseDTO shareDTO,
                                                          MemberSummaryDTO hostSummary,
                                                          MetaInfo metaInfo) {
        return new SharePropertyWithMemberResponseDTO(
                shareDTO.id(),
                shareDTO.kind(),
                shareDTO.sharePropertySubType(),
                shareDTO.displayStatus(),
                shareDTO.tradeStatus(),
                shareDTO.wishCount(),
                shareDTO.createdAt(),
                shareDTO.lastModifiedAt(),
                shareDTO.memberId(),
                shareDTO.optionItems(),
                shareDTO.genderPreference(),
                shareDTO.lgbtAvailable(),
                shareDTO.region(),
                shareDTO.photoUrls(),
                shareDTO.thumbnailUrl(),
                shareDTO.costDetails(),
                shareDTO.livingConditions(),
                shareDTO.moveInInfo(),
                shareDTO.description(),
                shareDTO.internalDetails(),
                shareDTO.capacityShare(),
                hostSummary,
                metaInfo
        );
    }
}
