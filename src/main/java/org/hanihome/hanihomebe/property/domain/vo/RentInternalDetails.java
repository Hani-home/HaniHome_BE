package org.hanihome.hanihomebe.property.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Embeddable
public class RentInternalDetails {
    @Column(nullable = false)
    private Double internalArea;

    private Double totalArea;

    @Column(nullable = false)
    private Integer numberOfRoom;

    @Column(nullable = false)
    private Integer numberOfBath;

    private Integer totalFloors;

    private Integer propertyFloors;

}
