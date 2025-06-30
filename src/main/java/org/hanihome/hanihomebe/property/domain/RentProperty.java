package org.hanihome.hanihomebe.property.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.enums.CapacityRent;
import org.hanihome.hanihomebe.property.domain.enums.Exposure;
import org.hanihome.hanihomebe.property.domain.enums.RealEstateType;
import org.hanihome.hanihomebe.property.domain.enums.RentPropertySubType;
import org.hanihome.hanihomebe.property.web.dto.PropertyPatchRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.RentPropertyCreateRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.RentPropertyPatchRequestDTO;

@Entity
@DiscriminatorValue("RENT")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder @PrimaryKeyJoinColumn(name = "property_id")
public class RentProperty extends Property {

    /** 1. 매물 유형 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RentPropertySubType rentPropertySubType;

    /** 2.  부동산 중개 여부*/
    @Enumerated(EnumType.STRING)
    private RealEstateType isRealEstateIntervention;

    /** 3. 거주 인원  */
    private CapacityRent capacityRent;

    /** 4. 남향,북향 */
    private Exposure exposure;

    public static RentProperty create(RentPropertyCreateRequestDTO dto, Member member) {
        return RentProperty.builder()
                .member(member)
                .kind(dto.kind())
                .genderPreference(dto.genderPreference())
                .region(dto.region())
                .photoUrls(dto.photoUrls())
                .weeklyCost(dto.weeklyCost())
                .isBillIncluded(dto.billIncluded())
                .costDescription(dto.costDescription())
                .deposit(dto.deposit())
                .keyDeposit(dto.keyDeposit())
                .noticePeriodWeeks(dto.noticePeriodWeeks())
                .minimumStayWeeks(dto.minimumStayWeeks())
                .contractTerms(dto.contractTerms())
                .availableFrom(dto.availableFrom())
                .availableTo(dto.availableTo())
                .isImmediate(dto.immediate())
                .isNegotiable(dto.negotiable())
                .parkingOption(dto.parkingOption())
                .possibleMeetingDates(dto.possibleMeetingDates())
                .description(dto.description())
                .rentPropertySubType(dto.rentPropertySubType())             // 고유필드 1
                .isRealEstateIntervention(dto.isRealEstateIntervention())   // 고유필드 2
                .capacityRent(dto.capacityRent())                           // 고유필드 3
                .exposure(dto.exposure())                                   // 고유필드 4
                .build();
    }

    @Override
    public Property update(PropertyPatchRequestDTO dto) {
        if(!(dto instanceof RentPropertyPatchRequestDTO)) throw new RuntimeException("타입 미스매칭");
        RentPropertyPatchRequestDTO rentDTO = (RentPropertyPatchRequestDTO) dto;
        // 자식 필드 업데이트
        if (rentDTO.getRentPropertySubType() != null) this.rentPropertySubType = rentDTO.getRentPropertySubType();
        /*if (rentDTO.getIsRealEstateIntervention() != null)
            this.isRealEstateIntervention = rentDTO.getIsRealEstateIntervention();*/
        // 부모 필드 업데이트
        super.updateBase(dto);

        return this;
    }
}

