package org.hanihome.hanihomebe.global.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;

@AllArgsConstructor
@Builder
@Getter
public class ErrorResponseDTO {
    private int statusCode;
    private String message;
    private String codeName;

    public static ErrorResponseDTO of(ServiceCode serviceCode) {
        return ErrorResponseDTO.builder()
                .statusCode(serviceCode.getHttpStatus().value())
                .message(serviceCode.getMessage())
                .codeName(serviceCode.name())
                .build();
    }
}
