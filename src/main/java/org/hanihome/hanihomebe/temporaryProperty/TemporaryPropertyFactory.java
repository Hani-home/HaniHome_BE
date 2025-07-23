package org.hanihome.hanihomebe.temporaryProperty;

import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.property.web.dto.request.create.PropertyCreateRequestDTO;
import org.hanihome.hanihomebe.temporaryProperty.domain.TemporaryProperty;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.TemporaryPropertyStepSaveRequestDTO;

public interface TemporaryPropertyFactory {
    boolean supports(PropertySuperType kind);
    TemporaryProperty create(TemporaryPropertyStepSaveRequestDTO dto, Member member);
}
