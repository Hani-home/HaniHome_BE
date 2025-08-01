package org.hanihome.hanihomebe.temporaryProperty.web.dto.create;


import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.property.domain.enums.GenderPreference;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.property.domain.vo.TimeSlot;
import org.hanihome.hanihomebe.property.domain.vo.ViewingAvailableDateTime;
import org.hanihome.hanihomebe.property.web.dto.request.create.CostDetailsDTO;
import org.hanihome.hanihomebe.property.web.dto.request.create.LivingConditionsDTO;
import org.hanihome.hanihomebe.property.web.dto.request.create.MoveInInfoDTO;
import org.hanihome.hanihomebe.temporaryProperty.domain.vo.TemporaryCostDetails;

import java.time.LocalDate;
import java.util.List;

//JackSon 직렬화 설정
public interface TemporaryPropertyCreateRequestDTO {//seal하면 좋을 듯
    Long memberId();//getter 명시 record에서 쓸 때 유용

    PropertySuperType kind();

    GenderPreference genderPreference();

    boolean lgbtAvailable();

    Region region();

    List<String> photoUrls();

    CostDetailsDTO costDetails();

    List<Long> optionItemIds();

    LivingConditionsDTO livingConditions();

    //이것도 temporary용으로 따로....?
    MoveInInfoDTO moveInInfo();

    LocalDate meetingDateFrom();
    LocalDate meetingDateTo();
    List<TimeSlot> timeSlots();
    List<ViewingAvailableDateTime> viewingAvailableDateTimes();
    boolean viewingAlwaysAvailable();

    String description();
}
