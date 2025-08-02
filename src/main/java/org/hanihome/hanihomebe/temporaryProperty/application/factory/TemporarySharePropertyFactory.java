package org.hanihome.hanihomebe.temporaryProperty.application.factory;

import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.temporaryProperty.domain.TemporaryProperty;
import org.hanihome.hanihomebe.temporaryProperty.domain.TemporaryShareProperty;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.create.TemporaryPropertyCreateRequestDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.create.TemporarySharePropertyCreateRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class TemporarySharePropertyFactory implements TemporaryPropertyFactory {

    @Override
    public boolean supports(PropertySuperType kind) {
        return kind == PropertySuperType.SHARE;
    }

    @Override
    public TemporaryProperty create(TemporaryPropertyCreateRequestDTO dto, Member member) {
        TemporarySharePropertyCreateRequestDTO shareDto = (TemporarySharePropertyCreateRequestDTO) dto;
        return TemporaryShareProperty.create(shareDto, member);
    }
}
