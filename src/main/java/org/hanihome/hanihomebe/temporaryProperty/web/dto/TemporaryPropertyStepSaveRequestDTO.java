package org.hanihome.hanihomebe.temporaryProperty.web.dto;

import lombok.Getter;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.temporaryProperty.domain.enums.TemporaryPropertyStepStatus;

@Getter
public class TemporaryPropertyStepSaveRequestDTO {
    private Long id; //저장 기록을 처음 생성하는 매물이면 null, 이전에 저장하는 걸 다시 저장하면 !null
    private TemporaryPropertyStepStatus stepStatus;
    private PropertySuperType kind;

    //1단계: 주소와 사진
    private AddressAndPhotosDTO addressAndPhotos;

    //2단계가 타입별로 다름
    private DetailDTO detail;
    //3단계: 입주조건
    private ConditionDTO condition;

    //4단계: 계약사항
    private ContractDTO contract;

}
