package org.hanihome.hanihomebe.property.web.dto.response;

import java.time.LocalTime;

public record TimeWithReserved(
        LocalTime time,
        boolean reserved
) {

}
