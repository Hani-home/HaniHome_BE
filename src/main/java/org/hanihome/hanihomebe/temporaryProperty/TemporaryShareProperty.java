package org.hanihome.hanihomebe.temporaryProperty;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hanihome.hanihomebe.property.domain.enums.CapacityShare;
import org.hanihome.hanihomebe.property.domain.enums.SharePropertySubType;
import org.hanihome.hanihomebe.property.domain.vo.ShareInternalDetails;

import java.time.temporal.TemporalAccessor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "temporary_share_property")
@SuperBuilder
@Getter
@NoArgsConstructor(access = PROTECTED)
@PrimaryKeyJoinColumn(name = "temporary_property_id")//부모 테이블의 PK를 그대로 사용함과 동시에 FK
public class TemporaryShareProperty extends TemporaryProperty {

    //2. 매물상세 (세컨트룸, 마스터룸, 거실 쉐어)
    @Enumerated(EnumType.STRING)
    private SharePropertySubType sharePropertySubType;

    @Enumerated(EnumType.STRING)
    private CapacityShare  capacityShare;

    //2. 매물 상세(내부 면적, 총 면적, 총 거주인, 욕실 쉐어자 수, 건물 전체 층, 해당 층)
    @Embedded
    private ShareInternalDetails shareInternalDetails;


}
