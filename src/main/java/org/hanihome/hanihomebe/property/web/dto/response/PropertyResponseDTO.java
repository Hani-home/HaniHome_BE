package org.hanihome.hanihomebe.property.web.dto.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.hanihome.hanihomebe.property.domain.vo.CostDetails;
import org.hanihome.hanihomebe.property.domain.vo.LivingConditions;
import org.hanihome.hanihomebe.property.domain.vo.MoveInInfo;
import org.hanihome.hanihomebe.property.domain.enums.*;

import java.time.LocalDateTime;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property="type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SharePropertyResponseDTO.class, name = "SHARE"),
        @JsonSubTypes.Type(value = RentPropertyResponseDTO.class, name = "RENT")
})
public sealed interface PropertyResponseDTO
    extends PropertyDTOByView
        permits SharePropertyResponseDTO, RentPropertyResponseDTO {

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
    ParkingOption parkingOption();
    String description();
}
