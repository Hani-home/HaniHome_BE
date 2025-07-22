package org.hanihome.hanihomebe.temporaryProperty;

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
import org.hanihome.hanihomebe.property.domain.RentProperty;
import org.hanihome.hanihomebe.property.domain.enums.CapacityRent;
import org.hanihome.hanihomebe.property.domain.enums.Exposure;
import org.hanihome.hanihomebe.property.domain.enums.RealEstateType;
import org.hanihome.hanihomebe.property.domain.enums.RentPropertySubType;
import org.hanihome.hanihomebe.property.domain.vo.RentInternalDetails;

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

}
