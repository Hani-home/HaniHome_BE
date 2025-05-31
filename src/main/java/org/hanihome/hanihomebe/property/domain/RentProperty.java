package org.hanihome.hanihomebe.property.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hanihome.hanihomebe.property.domain.enums.RentPropertySubType;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@DiscriminatorValue("RENT")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class RentProperty extends Property {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "rent_property_id")
    private Long id;

    /** 2. 매물 유형 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RentPropertySubType rentPropertySubType;

    private boolean isRealEstateIntervention;

}

