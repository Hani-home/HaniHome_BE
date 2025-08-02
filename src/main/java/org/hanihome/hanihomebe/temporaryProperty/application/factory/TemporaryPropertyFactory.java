package org.hanihome.hanihomebe.temporaryProperty.application.factory;

import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.temporaryProperty.domain.TemporaryProperty;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.create.TemporaryPropertyCreateRequestDTO;

public interface TemporaryPropertyFactory {
    boolean supports(PropertySuperType kind);
    TemporaryProperty create(TemporaryPropertyCreateRequestDTO dto, Member member);
}
