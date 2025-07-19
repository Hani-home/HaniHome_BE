package org.hanihome.hanihomebe.property.application.factory;


import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.domain.RentProperty;
import org.hanihome.hanihomebe.property.web.dto.request.create.PropertyCreateRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.request.create.RentPropertyCreateRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class RentPropertyFactory implements PropertyFactory{
    @Override
    public boolean supports(PropertyCreateRequestDTO dto) {
        return dto instanceof RentPropertyCreateRequestDTO;
    }

    @Override
    public Property create(PropertyCreateRequestDTO dto, Member member) {
        return RentProperty.create((RentPropertyCreateRequestDTO) dto, member);
    }

}
