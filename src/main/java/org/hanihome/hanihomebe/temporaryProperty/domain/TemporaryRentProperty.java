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
import org.hanihome.hanihomebe.property.domain.enums.Exposure;
import org.hanihome.hanihomebe.property.domain.enums.RealEstateType;
import org.hanihome.hanihomebe.property.domain.enums.RentPropertySubType;
import org.hanihome.hanihomebe.property.domain.vo.RentInternalDetails;
import org.hanihome.hanihomebe.property.web.dto.request.create.PropertyCreateRequestDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.AddressAndPhotosDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.ConditionDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.ContractDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.RentDetailDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.ShareDetailDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.TemporaryPropertyStepSaveRequestDTO;

import static org.hanihome.hanihomebe.property.domain.enums.PropertySuperType.RENT;
import static org.hanihome.hanihomebe.property.domain.enums.PropertySuperType.SHARE;

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

    @Enumerated(EnumType.STRING)
    private Exposure exposure;

    public static TemporaryRentProperty create(TemporaryPropertyStepSaveRequestDTO dto, Member member) {
        AddressAndPhotosDTO addressAndPhotosDTO = dto.getAddressAndPhotos();
        RentDetailDTO detailDTO = (RentDetailDTO) dto.getDetail();
        ConditionDTO conditionDTO = dto.getCondition();
        ContractDTO contractDTO = dto.getContract();


        return TemporaryRentProperty.builder()
                .member(member)
                .kind(RENT)
                .status(dto.getStepStatus())
                // 1단계
                .region(addressAndPhotosDTO.region())
                .photoUrls(addressAndPhotosDTO.imageUrls())
                // 2단계
                .rentPropertySubType(detailDTO.rentPropertySubType())
                .capacityRent(detailDTO.capacityRent())
                .rentInternalDetails(detailDTO.rentInternalDetails())
                .isRealEstateType(detailDTO.isRealEstateType())
                .exposure(detailDTO.exposure())
                //옵션 아이템은?? 장점이랑
                //3단계
                .genderPreference(conditionDTO.genderPreference())
                .lgbtAvailable(conditionDTO.lgbtAvailable())
                .livingConditions(conditionDTO.livingConditions())
                .moveInInfo(conditionDTO.moveInInfo())
                //흡연자 반료동물 이런거 OptionItem에 있는데 어떡하지
                //4단계
                .costDetails(contractDTO.costDetails())
                //빌에 포함된 친구들...
                .meetingDateFrom(contractDTO.meetingDateFrom())
                .meetingDateTo(contractDTO.meetingDateTo())
                .timeSlots(contractDTO.timeSlots())
                .viewingAlwaysAvailable(contractDTO.viewingAlwaysAvailable())

                .build();
    }

}
