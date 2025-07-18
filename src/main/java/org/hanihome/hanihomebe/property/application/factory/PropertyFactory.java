package org.hanihome.hanihomebe.property.application.factory;

import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.web.dto.request.PropertyCreateRequestDTO;

public interface PropertyFactory {
    boolean supports(PropertyCreateRequestDTO dto);

    Property create(PropertyCreateRequestDTO dto, Member member);
}
