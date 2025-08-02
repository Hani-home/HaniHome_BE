package org.hanihome.hanihomebe.temporaryProperty.domain.vo.temporaryDetails;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Embeddable
public class TemporaryRentInternalDetails {
    private Double internalArea;

    private Double totalArea;

    private Integer numberOfRoom;


    private Integer numberOfBath;

    private Integer totalFloors;

    private Integer propertyFloors;

    private boolean yardIncluded;

    private boolean verandaIncluded;
}
