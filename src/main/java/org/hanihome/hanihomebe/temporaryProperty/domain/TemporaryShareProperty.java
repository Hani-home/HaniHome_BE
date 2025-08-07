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
import org.hanihome.hanihomebe.temporaryProperty.domain.vo.TemporaryCostDetails;
import org.hanihome.hanihomebe.temporaryProperty.domain.vo.TemporaryLivingConditions;
import org.hanihome.hanihomebe.temporaryProperty.domain.vo.TemporaryMoveInInfo;
import org.hanihome.hanihomebe.temporaryProperty.domain.vo.temporaryDetails.TemporaryShareInternalDetails;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.create.TemporaryPropertyCreateRequestDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.create.TemporarySharePropertyCreateRequestDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.response.TemporaryPropertyResponseDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.response.TemporarySharePropertyResponseDTO;

import static lombok.AccessLevel.PROTECTED;

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
    private TemporaryShareInternalDetails shareInternalDetails;

    public static TemporaryShareProperty create(TemporarySharePropertyCreateRequestDTO dto, Member member) {
        //region 확인
        return TemporaryShareProperty.builder()
                .member(member)
                .kind(dto.kind())
                .status(dto.status())
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
                .sharePropertySubType(dto.sharePropertySubType())   // 고유필드 1
                .shareInternalDetails(dto.internalDetails())
                .capacityShare(dto.capacityShare())               // 고유필드 3
                .build();

    }

    @Override
    public TemporaryShareProperty update(TemporaryPropertyCreateRequestDTO dto) {
        TemporarySharePropertyCreateRequestDTO shareDto = (TemporarySharePropertyCreateRequestDTO) dto;

        super.updateBase(dto);

        this.sharePropertySubType = shareDto.sharePropertySubType();
        this.capacityShare = shareDto.capacityShare();
        this.shareInternalDetails = shareDto.internalDetails();

        return this;

    }

    @Override
    public TemporaryPropertyResponseDTO toResponseDTO() {
        return TemporarySharePropertyResponseDTO.fromEntity(this);
    }

}
