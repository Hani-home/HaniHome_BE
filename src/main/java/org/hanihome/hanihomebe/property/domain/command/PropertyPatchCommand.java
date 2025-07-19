package org.hanihome.hanihomebe.property.domain.command;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.property.domain.vo.CostDetails;
import org.hanihome.hanihomebe.property.domain.vo.LivingConditions;
import org.hanihome.hanihomebe.property.domain.vo.MoveInInfo;
import org.hanihome.hanihomebe.property.domain.vo.TimeSlot;
import org.hanihome.hanihomebe.property.domain.enums.*;
import org.hanihome.hanihomebe.property.domain.item.PropertyOptionItem;

import java.time.LocalDate;
import java.util.List;


@Getter
@SuperBuilder
public abstract class PropertyPatchCommand {
    private GenderPreference genderPreference;
    private Boolean lgbtAvailable;
    private Region region;
    private List<String> photoUrls;
    private CostDetails costDetails;
    private List<PropertyOptionItem> optionItems;
    private LivingConditions livingConditions;
    private MoveInInfo moveInInfo;
    private LocalDate meetingDateFrom;
    private LocalDate meetingDateTo;
    private List<TimeSlot> timeSlots;
    private Boolean viewingAlwaysAvailable;
    private String description;
    private DisplayStatus displayStatus;
    private TradeStatus tradeStatus;

}

