package org.hanihome.hanihomebe.property.web.dto.request.patch;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.property.domain.vo.CostDetails;
import org.hanihome.hanihomebe.property.domain.vo.LivingConditions;
import org.hanihome.hanihomebe.property.domain.vo.MoveInInfo;
import org.hanihome.hanihomebe.property.domain.vo.TimeSlot;
import org.hanihome.hanihomebe.property.domain.command.PropertyPatchCommand;
import org.hanihome.hanihomebe.property.domain.enums.*;
import org.hanihome.hanihomebe.property.domain.item.PropertyOptionItem;

import java.time.LocalDate;
import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "jsonDiscriminator"      // "jsonCriminator"라는 필드가 요청 바디에 필드로 포함되어있어야함
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SharePropertyPatchRequestDTO.class, name = "SHARE"),
        @JsonSubTypes.Type(value = RentPropertyPatchRequestDTO.class, name = "RENT")
})
@Getter
public abstract class PropertyPatchRequestDTO {
    private GenderPreference genderPreference;
    private Boolean lgbtAvailable;
    private Region region;
    private List<String> photoUrls;
    private CostDetails costDetails;
    private List<Long> optionItemIds;     // 포함된 비용 항목 리스트
    private LivingConditions livingConditions;
    private MoveInInfo moveInInfo;
    private LocalDate meetingDateFrom;
    private LocalDate meetingDateTo;
    private List<TimeSlot> timeSlots;
    private Boolean viewingAlwaysAvailable;
    private String description;
    private DisplayStatus displayStatus;

    public abstract PropertyPatchCommand toCommand(List<PropertyOptionItem> propertyOptionItems);
}
