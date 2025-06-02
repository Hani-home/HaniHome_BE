package org.hanihome.hanihomebe.property.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.enums.SharePropertySubType;
import org.hanihome.hanihomebe.property.web.dto.PropertyPatchRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.SharePropertyCreateRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.SharePropertyPatchRequestDTO;
import org.springframework.data.mapping.PropertyPath;

@Entity
@DiscriminatorValue("SHARE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder @PrimaryKeyJoinColumn(name = "property_id")
public class ShareProperty extends Property {


    /**
     * 2. 매물 유형
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SharePropertySubType sharePropertySubType;

    public static ShareProperty create(SharePropertyCreateRequestDTO dto, Member member) {
        return ShareProperty.builder()
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
                .sharePropertySubType(dto.sharePropertySubType())
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

