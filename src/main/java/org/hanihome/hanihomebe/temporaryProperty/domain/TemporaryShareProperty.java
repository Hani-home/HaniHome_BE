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
import org.hanihome.hanihomebe.temporaryProperty.web.dto.RentDetailDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.ShareDetailDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.TemporaryPropertyStepSaveRequestDTO;

import static lombok.AccessLevel.PROTECTED;
import static org.hanihome.hanihomebe.property.domain.enums.PropertySuperType.RENT;

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

        return TemporaryShareProperty.builder()
                .member(member)
                .kind(RENT)
                .status(dto.getStepStatus())
                // 1단계
                .region(addressAndPhotosDTO.region())
                .photoUrls(addressAndPhotosDTO.imageUrls())
                // 2단계
                .sharePropertySubType(detailDTO.sharePropertySubType())
                .capacityShare(detailDTO.capacityShare())
                .shareInternalDetails(detailDTO.shareInternalDetails())
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
