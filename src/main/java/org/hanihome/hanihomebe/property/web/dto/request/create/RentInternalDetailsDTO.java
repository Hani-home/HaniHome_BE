package org.hanihome.hanihomebe.property.web.dto.request.create;

import jakarta.persistence.Column;
import org.hanihome.hanihomebe.property.domain.vo.RentInternalDetails;

public record RentInternalDetailsDTO(
        Double internalArea,
        Double totalArea,
        Integer numberOfRoom,
        Integer numberOfBath,
        Integer totalFloors,
        Integer propertyFloors,
        boolean yardIncluded,
        boolean verandaIncluded
) {
    public RentInternalDetails toVO() {
        return new RentInternalDetails(
                internalArea,
                totalArea,
                numberOfRoom,
                numberOfBath,
                totalFloors,
                propertyFloors,
                yardIncluded,
                verandaIncluded);
    }
}
