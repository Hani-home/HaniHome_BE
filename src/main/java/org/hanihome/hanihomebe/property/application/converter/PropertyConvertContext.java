package org.hanihome.hanihomebe.property.application.converter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.hanihome.hanihomebe.member.web.dto.MemberSummaryDTO;
import org.hanihome.hanihomebe.metro.web.dto.nearest.NearestMetroStopResponseDTO;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.web.dto.response.summary.MetaInfo;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class PropertyConvertContext {
    private Property property;
    private List<OptionItemResponseDTO> optionItems;
    private NearestMetroStopResponseDTO nearestMetroStopResponseDTO;
    private MemberSummaryDTO hostSummaryDTO;
    private MetaInfo metaInfo;

    public static PropertyConvertContext create(Property property,
                                                List<OptionItemResponseDTO> optionItems,
                                                NearestMetroStopResponseDTO nearestMetroStopResponseDTO,
                                                MemberSummaryDTO hostSummaryDTO,
                                                MetaInfo metaInfo
    ) {
        return PropertyConvertContext.builder()
                .property(property)
                .optionItems(optionItems)
                .nearestMetroStopResponseDTO(nearestMetroStopResponseDTO)
                .hostSummaryDTO(hostSummaryDTO)
                .metaInfo(metaInfo)
                .build();
    }
}
