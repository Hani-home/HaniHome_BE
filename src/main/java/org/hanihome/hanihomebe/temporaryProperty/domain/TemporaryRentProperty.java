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
import org.hanihome.hanihomebe.property.domain.vo.RentInternalDetails;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.AddressAndPhotosDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.DescriptionDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.ConditionDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.ContractDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.RentDetailDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.TemporaryPropertyStepSaveRequestDTO;
import static org.hanihome.hanihomebe.property.domain.enums.PropertySuperType.RENT;


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
    private RentInternalDetails rentInternalDetails;

    //2. 매물 상세
    @Enumerated(EnumType.STRING)
    private RealEstateType isRealEstateType;


    public static TemporaryRentProperty create(TemporaryPropertyStepSaveRequestDTO dto, Member member) {
        AddressAndPhotosDTO addressAndPhotosDTO = dto.getAddressAndPhotos();
        RentDetailDTO detailDTO = (RentDetailDTO) dto.getDetail();
        ConditionDTO conditionDTO = dto.getCondition();
        ContractDTO contractDTO = dto.getContract();
        DescriptionDTO descriptionDTO = dto.getDescription();

        TemporaryRentProperty.TemporaryRentPropertyBuilder builder =
                TemporaryRentProperty.builder()
                        .member(member)
                        .kind(RENT)
                        .status(dto.getStepStatus());

        if(addressAndPhotosDTO != null) {
            builder
                    .region(addressAndPhotosDTO.region())
                    .photoUrls(addressAndPhotosDTO.imageUrls());
        }

        // 2단계
        if (detailDTO != null) {
            builder
                    .rentPropertySubType(detailDTO.rentPropertySubType())
                    .capacityRent(detailDTO.capacityRent())
                    .rentInternalDetails(detailDTO.rentInternalDetails())
                    .isRealEstateType(detailDTO.isRealEstateType());
        }

        // 3단계
        if (conditionDTO != null) {
            builder
                    .genderPreference(conditionDTO.genderPreference())
                    .lgbtAvailable(conditionDTO.lgbtAvailable())
                    .livingConditions(conditionDTO.livingConditions())
                    .moveInInfo(conditionDTO.moveInInfo());
        }

        // 4단계
        if (contractDTO != null) {
            builder
                    .costDetails(contractDTO.costDetails())
                    .meetingDateFrom(contractDTO.meetingDateFrom())
                    .meetingDateTo(contractDTO.meetingDateTo())
                    .timeSlots(contractDTO.timeSlots())
                    .viewingAlwaysAvailable(contractDTO.viewingAlwaysAvailable());
        }

        if(descriptionDTO != null) {
            builder
                    .description(descriptionDTO.description());
        }

        return builder.build();

        //null 기준이 아니고 완료된 단계까지 검사를 해야함...
    }
}
