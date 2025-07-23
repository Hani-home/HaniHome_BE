package org.hanihome.hanihomebe.temporaryProperty.web.dto;

import org.hanihome.hanihomebe.property.domain.enums.GenderPreference;
import org.hanihome.hanihomebe.property.domain.vo.LivingConditions;
import org.hanihome.hanihomebe.property.domain.vo.MoveInInfo;

import java.util.List;

public record ConditionDTO(
        GenderPreference genderPreference,
        boolean lgbtAvailable,
        LivingConditions livingConditions,
        MoveInInfo moveInInfo,
        List<Long> conditionOptionItemIds //흡연자/반려동물/주차 등 가능여부인데 OptionItem에 저장되어 있음
) {
}
