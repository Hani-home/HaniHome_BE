package org.hanihome.hanihomebe.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.global.response.dto.CommonResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@Order(1)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CommonResponse> handleCustomException(CustomException e) {
        log.info("globalExceptionHandler에 진입" + e.getMessage());
        ServiceCode serviceCode = e.getServiceCode();
        log.error("CustomException 발생: 상태코드={}, 메시지={}, 에러명={}",
                serviceCode.getHttpStatus().value(),
                serviceCode.getMessage(),
                serviceCode.name());

        CommonResponse<ErrorResponseDTO> commonResponse = CommonResponse.failure(ErrorResponseDTO.of(serviceCode), serviceCode);
        return ResponseEntity.status(serviceCode.getHttpStatus().value())
                .body(commonResponse);
    }
}
