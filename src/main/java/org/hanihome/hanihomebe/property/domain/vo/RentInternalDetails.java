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

    private Double internalArea;

    private Double totalArea;

    private Integer numberOfRoom;


    private Integer numberOfBath;

    private Integer totalFloors;

    private Integer propertyFloors;

}
