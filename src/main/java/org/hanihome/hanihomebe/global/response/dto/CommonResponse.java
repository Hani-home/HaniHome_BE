package org.hanihome.hanihomebe.global.response.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;

@AllArgsConstructor
@Getter
@Builder
public class CommonResponse<T> {
    private boolean isSuccess;  // 성공여부
    private String serviceCode; // http상태코드가 아닌 자체 서비스 코드
    private String message;     // message
    private T data;             // 반환값

    // GlobalResponseWrapper용 Success
    public static <T> CommonResponse<T> success(T data) {
        return CommonResponse.<T>builder()
                .isSuccess(true)
                .serviceCode(ServiceCode.SUCCESS.name())
                .message(ServiceCode.SUCCESS.getMessage())
                .data(data)
                .build();
    }

    public static <T> CommonResponse<T> success(ServiceCode code, T data) {
        return CommonResponse.<T>builder()
                .isSuccess(true)
                .serviceCode(code.name())
                .message(code.getMessage())
                .data(data)
                .build();
    }

    // GlobalResponseWrapper용 failure
    public static <T> CommonResponse<T> failure(T data) {
        return CommonResponse.<T>builder()
                .isSuccess(false)
                .serviceCode(ServiceCode.NOT_DEFINED_ERROR_FROM_WRAPPER.name())
                .message(ServiceCode.NOT_DEFINED_ERROR_FROM_WRAPPER.getMessage())
                .data(data)
                .build();
    }

    public static <T> CommonResponse<T> failure(T data, ServiceCode code) {
        return CommonResponse.<T>builder()
                .isSuccess(false)
                .serviceCode(code.name())
                .message(code.getMessage())
                .data(data)
                .build();
    }

    public static <T> CommonResponse<T> failure(ServiceCode code) {
        return CommonResponse.<T>builder()
                .isSuccess(false)
                .serviceCode(code.name())
                .message(code.getMessage())
                .data(null)
                .build();
    }
}
