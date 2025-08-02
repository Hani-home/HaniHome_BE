package org.hanihome.hanihomebe.property.web.dto.request.create;

import org.hanihome.hanihomebe.property.domain.vo.ShareInternalDetails;

public record ShareInternalDetailsDTO(
         Double internalArea,
         Double totalArea,
         Integer totalResidents,
         Integer totalBathUser,
         Integer totalFloors,
         Integer propertyFloor,
         boolean withPropertyOwner
) {
    public ShareInternalDetails toVO() {
        return new ShareInternalDetails(
                internalArea,
                totalArea,
                totalResidents,
                totalBathUser,
                totalFloors,
                propertyFloor,
                withPropertyOwner
        );
    }
}
