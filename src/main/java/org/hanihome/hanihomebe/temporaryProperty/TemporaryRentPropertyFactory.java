package org.hanihome.hanihomebe.temporaryProperty;

import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.application.factory.RentPropertyFactory;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.temporaryProperty.domain.TemporaryProperty;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.RentDetailDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.TemporaryPropertyStepSaveRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class TemporaryRentPropertyFactory implements TemporaryPropertyFactory {

    @Override
    public boolean supports(PropertySuperType kind) {
        return kind == PropertySuperType.RENT;
    }

    @Override
    public TemporaryProperty create(TemporaryPropertyStepSaveRequestDTO dto, Member member) {
        RentDetailDTO detail = (RentDetailDTO) dto.getDetail(); //형변환 Detail => RentDetailDTO
    }
}
