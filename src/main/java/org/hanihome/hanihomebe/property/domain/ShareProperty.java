package org.hanihome.hanihomebe.property.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.command.PropertyPatchCommand;
import org.hanihome.hanihomebe.property.domain.command.SharePropertyPatchCommand;
import org.hanihome.hanihomebe.property.domain.enums.CapacityShare;
import org.hanihome.hanihomebe.property.domain.enums.SharePropertySubType;
import org.hanihome.hanihomebe.property.domain.vo.ShareInternalDetails;
import org.hanihome.hanihomebe.property.web.dto.request.SharePropertyCreateRequestDTO;

@Entity
@DiscriminatorValue("SHARE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder @PrimaryKeyJoinColumn(name = "property_id")
public class ShareProperty extends Property {


    /**
     * 1. 매물 유형
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SharePropertySubType sharePropertySubType;

    /**
     * 2. 매물 정보
     */
    @Embedded
    private ShareInternalDetails shareInternalDetails;

    /** 3. 수용인원 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CapacityShare capacityShare;

    public static ShareProperty create(SharePropertyCreateRequestDTO dto, Member member) {
        return ShareProperty.builder()
                .member(member)
                .kind(dto.kind())
                .genderPreference(dto.genderPreference())
                .lgbtAvailable(dto.lgbtAvailable())
                .region(dto.region())
                .photoUrls(dto.photoUrls())
                .costDetails(dto.costDetails())
                .livingConditions(dto.livingConditions())
                .moveInInfo(dto.moveInInfo())
                .parkingOption(dto.parkingOption())
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
    public Property update(PropertyPatchCommand cmd) {
        if(!(cmd instanceof SharePropertyPatchCommand)){
            throw new CustomException(ServiceCode.PROPERTY_PATCH_COMMAND_MISMATCH);
        }
        SharePropertyPatchCommand shareCmd = (SharePropertyPatchCommand) cmd;
        // 자식 필드 업데이트
        if(shareCmd.getSharePropertySubType()!=null) this.sharePropertySubType = shareCmd.getSharePropertySubType();

        if(shareCmd.getInternalDetails()!=null) this.shareInternalDetails = shareCmd.getInternalDetails();

        if(shareCmd.getCapacityShare()!=null) this.capacityShare = shareCmd.getCapacityShare();

        // 부모 필드 업데이트
        super.updateBase(cmd);

        return this;
    }

}