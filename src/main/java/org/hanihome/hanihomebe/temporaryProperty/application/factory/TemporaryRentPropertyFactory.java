package org.hanihome.hanihomebe.temporaryProperty.application.factory;

import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.temporaryProperty.domain.TemporaryProperty;
import org.hanihome.hanihomebe.temporaryProperty.domain.TemporaryRentProperty;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.create.TemporaryPropertyCreateRequestDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.create.TemporaryRentPropertyCreateRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class TemporaryRentPropertyFactory implements TemporaryPropertyFactory {

    @Override
    public boolean supports(PropertySuperType kind) {
        return kind == PropertySuperType.RENT;
    }

    @Override
    public TemporaryProperty create(TemporaryPropertyCreateRequestDTO dto, Member member) {
        TemporaryRentPropertyCreateRequestDTO RentDto = (TemporaryRentPropertyCreateRequestDTO) dto;
        return TemporaryRentProperty.create(RentDto, member);
    }
}
