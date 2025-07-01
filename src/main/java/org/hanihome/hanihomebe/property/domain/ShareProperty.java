package org.hanihome.hanihomebe.property.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.enums.CapacityShare;
import org.hanihome.hanihomebe.property.domain.enums.SharePropertySubType;
import org.hanihome.hanihomebe.property.web.dto.PropertyPatchRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.SharePropertyCreateRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.SharePropertyPatchRequestDTO;

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
    private Double internalArea;     // 2-1. 실제 사용 면적
    private Double totalArea;        // 2-2. 전체 면적
    
    // 총 거주인
    private Integer totalResidents;  // 2-3. 총 거주 인원
    
    // 욕실 쉐어자 수
    private Integer totalBathUser;   // 2-4. 욕실 공유 인원
    
    // 건물 전체 층
    private Integer totalFloors;     // 2-5. 건물 총 층수
    
    // 해당 층
    private Integer propertyFloor;   // 2-6. 해당 매물의 층수

    /** 3. 수용인원 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CapacityShare capacityShare;

    public static ShareProperty create(SharePropertyCreateRequestDTO dto, Member member) {
        return ShareProperty.builder()
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
                .meetingDateFrom(dto.meetingDateFrom())
                .meetingDateTo(dto.meetingDateTo())
                .timeSlots(dto.timeSlots())
                .description(dto.description())
                .sharePropertySubType(dto.sharePropertySubType())   // 고유필드 1
                .internalArea(dto.internalArea())                   // 2-1. 실제 사용 면적
                .totalArea(dto.totalArea())                        // 2-2. 전체 면적
                .totalResidents(dto.totalResidents())              // 2-3. 총 거주 인원
                .totalBathUser(dto.totalBathUser())               // 2-4. 욕실 공유 인원
                .totalFloors(dto.totalFloors())                    // 2-5. 건물 총 층수
                .propertyFloor(dto.propertyFloor())               // 2-6. 해당 매물의 층수
                .capacityShare(dto.capacityShare())               // 고유필드 3
                .build();
    }

    @Override
    public Property update(PropertyPatchRequestDTO dto) {
        if(!(dto instanceof SharePropertyPatchRequestDTO)){
            throw new RuntimeException("업데이트 DTO와 엔티티 타입이 미스매칭");
        }
        SharePropertyPatchRequestDTO shareDTO = (SharePropertyPatchRequestDTO) dto;
        // 자식 필드 업데이트
        if(shareDTO.getSharePropertySubType()!=null) this.sharePropertySubType = shareDTO.getSharePropertySubType();
        // 부모 필드 업데이트
        super.updateBase(dto);

        return this;
    }

}