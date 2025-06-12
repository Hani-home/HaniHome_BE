package org.hanihome.hanihomebe.property.web.dto;

import lombok.Getter;
import org.hanihome.hanihomebe.property.domain.enums.SharePropertySubType;

@Getter
public class SharePropertyPatchRequestDTO extends PropertyPatchRequestDTO {
    private SharePropertySubType sharePropertySubType;   // (ShareProperty 고유) 매물 유형
}
