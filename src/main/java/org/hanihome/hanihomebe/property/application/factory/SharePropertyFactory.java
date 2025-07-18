package org.hanihome.hanihomebe.property.application.factory;

import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.domain.ShareProperty;
import org.hanihome.hanihomebe.property.web.dto.request.PropertyCreateRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.request.SharePropertyCreateRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class SharePropertyFactory implements PropertyFactory{
    @Override
    public boolean supports(PropertyCreateRequestDTO dto) {
        return dto instanceof SharePropertyCreateRequestDTO;
    }

    @Override
    public Property create(PropertyCreateRequestDTO dto, Member member) {
        return ShareProperty.create((SharePropertyCreateRequestDTO) dto, member);
    }
}
