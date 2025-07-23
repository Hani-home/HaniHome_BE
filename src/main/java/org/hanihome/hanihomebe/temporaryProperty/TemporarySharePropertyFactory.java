package org.hanihome.hanihomebe.temporaryProperty;

import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.temporaryProperty.domain.TemporaryProperty;
import org.hanihome.hanihomebe.temporaryProperty.domain.TemporaryShareProperty;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.TemporaryPropertyStepSaveRequestDTO;

public class TemporarySharePropertyFactory implements TemporaryPropertyFactory {

    @Override
    public boolean supports(PropertySuperType kind) {
        return kind == PropertySuperType.SHARE;
    }

    @Override
    public TemporaryProperty create(TemporaryPropertyStepSaveRequestDTO dto, Member member) {
        return TemporaryShareProperty.create(dto, member);
    }
}
