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
import org.hanihome.hanihomebe.property.domain.command.RentPropertyPatchCommand;
import org.hanihome.hanihomebe.property.domain.enums.CapacityRent;
import org.hanihome.hanihomebe.property.domain.enums.Exposure;
import org.hanihome.hanihomebe.property.domain.enums.RealEstateType;
import org.hanihome.hanihomebe.property.domain.enums.RentPropertySubType;
import org.hanihome.hanihomebe.property.domain.vo.RentInternalDetails;
import org.hanihome.hanihomebe.property.web.dto.request.RentPropertyCreateRequestDTO;

@Entity
@DiscriminatorValue("RENT")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder @PrimaryKeyJoinColumn(name = "property_id")
public class RentProperty extends Property {

    /** 매물 유형 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RentPropertySubType rentPropertySubType;

    /**  부동산 중개 여부*/
    @Enumerated(EnumType.STRING)
    private RealEstateType isRealEstateIntervention;

    /**
     * 매물 정보
     */
    @Embedded
    private RentInternalDetails rentInternalDetails;

    /**  거주 인원  */
    private CapacityRent capacityRent;

    /**  남향,북향 */
    private Exposure exposure;

    public static RentProperty create(RentPropertyCreateRequestDTO dto, Member member) {
        return RentProperty.builder()
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
                .rentPropertySubType(dto.rentPropertySubType())             // 고유필드
                .rentInternalDetails(dto.internalDetails())                 // 고유필드
                .capacityRent(dto.capacityRent())                           // 고유필드
                .build();
    }

    @Override
    public Property update(PropertyPatchCommand cmd) {
        if (!(cmd instanceof RentPropertyPatchCommand)) {
            throw new CustomException(ServiceCode.PROPERTY_PATCH_COMMAND_MISMATCH);
        }
        RentPropertyPatchCommand rentCmd = (RentPropertyPatchCommand) cmd;
        // 자식 필드 업데이트
        if (rentCmd.getRentPropertySubType() != null) this.rentPropertySubType = rentCmd.getRentPropertySubType();

        if(rentCmd.getInternalDetails()!=null) this.rentInternalDetails = rentCmd.getInternalDetails();

        if(rentCmd.getCapacityRent()!=null) this.capacityRent = rentCmd.getCapacityRent();

        // 부모 필드 업데이트
        super.updateBase(cmd);

        return this;
    }
}

