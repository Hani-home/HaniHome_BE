package org.hanihome.hanihomebe.global.exception;

import lombok.Getter;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;

@Getter
public class CustomException extends RuntimeException{
    private ServiceCode serviceCode;

    public CustomException(ServiceCode serviceCode, Throwable cause) {
        super(cause);
        this.serviceCode = serviceCode;
    }

    public CustomException(ServiceCode serviceCode) {
        this.serviceCode = serviceCode;
    }
}
