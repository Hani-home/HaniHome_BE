package org.hanihome.hanihomebe.temporaryProperty.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.enums.CapacityRent;
import org.hanihome.hanihomebe.property.domain.enums.RealEstateType;
import org.hanihome.hanihomebe.property.domain.enums.RentPropertySubType;
import org.hanihome.hanihomebe.temporaryProperty.domain.vo.TemporaryCostDetails;
import org.hanihome.hanihomebe.temporaryProperty.domain.vo.TemporaryLivingConditions;
import org.hanihome.hanihomebe.temporaryProperty.domain.vo.TemporaryMoveInInfo;
import org.hanihome.hanihomebe.temporaryProperty.domain.vo.temporaryDetails.TemporaryRentInternalDetails;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.create.TemporaryRentPropertyCreateRequestDTO;


@Entity
@Table(name = "temporary_rent_property")
@PrimaryKeyJoinColumn(name = "temporary_property_id")
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TemporaryRentProperty extends TemporaryProperty {

    //2. 매물 상세
    @Enumerated(EnumType.STRING)
    private RentPropertySubType rentPropertySubType;

    //2. 매물 상세
    @Enumerated(EnumType.STRING)
    private CapacityRent capacityRent;

    //2. 매물 상세
    @Embedded
    private TemporaryRentInternalDetails rentInternalDetails;

    //2. 매물 상세
    @Enumerated(EnumType.STRING)
    private RealEstateType isRealEstateIntervention;

    public static TemporaryRentProperty create (TemporaryRentPropertyCreateRequestDTO dto, Member member) {//DTO에도 memberId가 있는데 따로 멤버를 받는 이유가 있나?
        //적어도 주소는 다 입력되었는지 검사하는 로직추가

        //근데 얘네들 null이여도 NPE 안뜨고 잘되나?
        return TemporaryRentProperty.builder()
                .member(member)
                .kind(dto.kind())
                .genderPreference(dto.genderPreference())
                .lgbtAvailable(dto.lgbtAvailable())
                .region(dto.region())
                .photoUrls(dto.photoUrls())
                .costDetails(dto.costDetails() != null
                        ? dto.costDetails().toTemporaryVO()
                        : TemporaryCostDetails.empty())
                .livingConditions(dto.livingConditions() != null
                        ? dto.livingConditions().toTemporaryVO()
                        : TemporaryLivingConditions.empty())
                .moveInInfo(dto.moveInInfo() != null
                        ? dto.moveInInfo().toTemporaryVO()
                        : TemporaryMoveInInfo.empty())
                .meetingDateFrom(dto.meetingDateFrom())
                .meetingDateTo(dto.meetingDateTo())
                .timeSlots(dto.timeSlots())
                .viewingAvailableDateTimes(dto.viewingAvailableDateTimes())
                .viewingAlwaysAvailable(dto.viewingAlwaysAvailable())
                .description(dto.description())
                .rentPropertySubType(dto.rentPropertySubType())             // 고유필드
                .rentInternalDetails(dto.internalDetails())                 // 고유필드
                .capacityRent(dto.capacityRent())                           // 고유필드
                .build();

    }
}
