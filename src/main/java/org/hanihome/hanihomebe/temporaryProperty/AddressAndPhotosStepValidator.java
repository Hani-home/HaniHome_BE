package org.hanihome.hanihomebe.temporaryProperty;

import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.compilermsgs.qual.CompilerMessageKey;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.temporaryProperty.domain.enums.TemporaryPropertyStepStatus;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.AddressAndPhotosDTO;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AddressAndPhotosStepValidator implements StepValidator<AddressAndPhotosDTO> {

    @Override
    public TemporaryPropertyStepStatus getStepStatus() {
        return TemporaryPropertyStepStatus.ADDRESS_AND_PHOTOS;
    }

    @Override
    public void validate(AddressAndPhotosDTO dto){
        if (dto.region() == null) {//null 검사로는 부족할 듯
            throw new CustomException(ServiceCode.INVALID_TEMPORARY_PROPERTY);
        }

        if (dto.imageUrls() == null || dto.imageUrls().isEmpty()) {
            throw new CustomException(ServiceCode.INVALID_TEMPORARY_PROPERTY);
        }
    }
}
