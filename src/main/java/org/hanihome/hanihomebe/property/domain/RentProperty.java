package org.hanihome.hanihomebe.property.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.enums.RentPropertySubType;
import org.hanihome.hanihomebe.property.web.dto.PropertyPatchRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.RentPropertyCreateRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.RentPropertyPatchRequestDTO;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@DiscriminatorValue("RENT")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder @PrimaryKeyJoinColumn(name = "property_id")
public class RentProperty extends Property {

    /** 2. 매물 유형 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RentPropertySubType rentPropertySubType;

    private boolean isRealEstateIntervention;

    public static RentProperty create(RentPropertyCreateRequestDTO dto, Member member) {
        return RentProperty.builder()
                .member(member)
                .kind(dto.kind())
                .capacity(dto.capacity())
                .genderPreference(dto.genderPreference())
                .region(dto.region())
                .photoUrls(dto.photoUrls())
                .weeklyCost(dto.weeklyCost())
                .costDescription(dto.costDescription())
                .deposit(dto.deposit())
                .keyDeposit(dto.keyDeposit())
                .noticePeriodWeeks(dto.noticePeriodWeeks())
                .minimumStayWeeks(dto.minimumStayWeeks())
                .contractTerms(dto.contractTerms())
                .availableFrom(dto.availableFrom())
                .parkingOption(dto.parkingOption())
                .viewingDates(dto.viewingDates())
                .description(dto.description())
                .isRealEstateIntervention(dto.isRealEstateIntervention())
                .rentPropertySubType(dto.rentPropertySubType())
                .build();
    }

    @Override
    public Property update(PropertyPatchRequestDTO dto) {
        if(!(dto instanceof RentPropertyPatchRequestDTO)) throw new RuntimeException("타입 미스매칭");
        RentPropertyPatchRequestDTO rentDTO = (RentPropertyPatchRequestDTO) dto;
        // 자식 필드 업데이트
        if (rentDTO.getRentPropertySubType() != null) this.rentPropertySubType = rentDTO.getRentPropertySubType();
        if (rentDTO.getIsRealEstateIntervention() != null)
            this.isRealEstateIntervention = rentDTO.getIsRealEstateIntervention();
        // 부모 필드 업데이트
        super.updateBase(dto);

        return this;
    }
}

