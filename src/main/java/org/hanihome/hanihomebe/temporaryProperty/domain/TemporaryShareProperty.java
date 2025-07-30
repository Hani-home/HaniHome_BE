package org.hanihome.hanihomebe.temporaryProperty.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.enums.CapacityShare;
import org.hanihome.hanihomebe.property.domain.enums.SharePropertySubType;
import org.hanihome.hanihomebe.property.domain.vo.ShareInternalDetails;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.AddressAndPhotosDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.ConditionDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.ContractDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.DescriptionDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.RentDetailDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.ShareDetailDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.TemporaryPropertyStepSaveRequestDTO;

import static lombok.AccessLevel.PROTECTED;
import static org.hanihome.hanihomebe.property.domain.enums.PropertySuperType.RENT;
import static org.hanihome.hanihomebe.property.domain.enums.PropertySuperType.SHARE;

@Entity
@Table(name = "temporary_share_property")
@SuperBuilder
@Getter
@NoArgsConstructor(access = PROTECTED)
@PrimaryKeyJoinColumn(name = "temporary_property_id")//부모 테이블의 PK를 그대로 사용함과 동시에 FK
public class TemporaryShareProperty extends TemporaryProperty {

    //2. 매물상세 (세컨트룸, 마스터룸, 거실 쉐어)
    @Enumerated(EnumType.STRING)
    private SharePropertySubType sharePropertySubType;

    @Enumerated(EnumType.STRING)
    private CapacityShare  capacityShare;

    //2. 매물 상세(내부 면적, 총 면적, 총 거주인, 욕실 쉐어자 수, 건물 전체 층, 해당 층)
    @Embedded
    private ShareInternalDetails shareInternalDetails;

    public static TemporaryShareProperty create(TemporaryPropertyStepSaveRequestDTO dto, Member member) {
        AddressAndPhotosDTO addressAndPhotosDTO = dto.getAddressAndPhotos();
        ShareDetailDTO detailDTO = (ShareDetailDTO) dto.getDetail();
        ConditionDTO conditionDTO = dto.getCondition();
        ContractDTO contractDTO = dto.getContract();
        DescriptionDTO descriptionDTO = dto.getDescription();

        TemporaryShareProperty.TemporarySharePropertyBuilder builder =
                TemporaryShareProperty.builder()
                        .member(member)
                        .kind(SHARE)
                        .status(dto.getStepStatus());

        //1단계
        if(addressAndPhotosDTO != null) {
            builder
                    .region(addressAndPhotosDTO.region())
                    .photoUrls(addressAndPhotosDTO.imageUrls());
        }

        // 2단계
        if (detailDTO != null) {
            builder
                    .sharePropertySubType(detailDTO.sharePropertySubType())
                    .capacityShare(detailDTO.capacityShare())
                    .shareInternalDetails(detailDTO.shareInternalDetails());
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

        //5단계
        if(descriptionDTO != null) {
            builder
                    .description(descriptionDTO.description());
        }

        return builder.build();



    }

}
