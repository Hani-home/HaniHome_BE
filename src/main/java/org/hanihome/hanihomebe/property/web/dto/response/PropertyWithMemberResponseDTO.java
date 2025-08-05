package org.hanihome.hanihomebe.property.web.dto.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.hanihome.hanihomebe.member.web.dto.MemberSummaryDTO;
import org.hanihome.hanihomebe.property.domain.enums.DisplayStatus;
import org.hanihome.hanihomebe.property.domain.enums.GenderPreference;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.property.domain.enums.TradeStatus;
import org.hanihome.hanihomebe.property.domain.vo.CostDetails;
import org.hanihome.hanihomebe.property.domain.vo.LivingConditions;
import org.hanihome.hanihomebe.property.domain.vo.MoveInInfo;
import org.hanihome.hanihomebe.property.web.dto.response.basic.RentPropertyResponseDTO;
import org.hanihome.hanihomebe.property.web.dto.response.basic.SharePropertyResponseDTO;
import org.hanihome.hanihomebe.property.web.dto.response.summary.MetaInfo;

import java.time.LocalDateTime;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property="type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SharePropertyResponseDTO.class, name = "SHARE"),
        @JsonSubTypes.Type(value = RentPropertyResponseDTO.class, name = "RENT")
})
public sealed interface PropertyWithMemberResponseDTO extends PropertyDTOByView
        permits SharePropertyWithMemberResponseDTO, RentPropertyWithMemberResponseDTO {

    Long id();
    int wishCount();
    LocalDateTime createdAt();
    LocalDateTime lastModifiedAt();
    Long memberId();
    List<OptionItemResponseDTO> optionItems();
    DisplayStatus displayStatus();
    TradeStatus tradeStatus();
    PropertySuperType kind();
    GenderPreference genderPreference();
    boolean lgbtAvailable();
    Region region();
    List<String> photoUrls();
    String thumbnailUrl();
    CostDetails costDetails();
    LivingConditions livingConditions();
    MoveInInfo moveInInfo();
    LocalDateTime meetingDateFrom();
    LocalDateTime meetingDateTo();
    String description();
    MemberSummaryDTO hostSummary();
    MetaInfo metaInfo();
}
