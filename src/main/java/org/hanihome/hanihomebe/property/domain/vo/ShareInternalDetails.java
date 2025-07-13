package org.hanihome.hanihomebe.property.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Embeddable
public class ShareInternalDetails {
    private Double internalArea;     // 2-1. 실제 사용 면적

    private Double totalArea;        // 2-2. 전체 면적

    private Integer totalResidents;  // 2-3. 총 거주 인원

    private Integer totalBathUser;   // 2-4. 욕실 공유 인원

    private Integer totalFloors;     // 2-5. 건물 총 층수

    private Integer propertyFloor;   // 2-6. 해당 매물의 층수
}
