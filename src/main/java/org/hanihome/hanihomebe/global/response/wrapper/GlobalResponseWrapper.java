package org.hanihome.hanihomebe.global.response.wrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.global.response.dto.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/*
* 기능: 응답의 body만 wrapping함.
* 그 외의 status, header를 건드는건 목적이 아님
* */

/*
@Order(2)
@RestControllerAdvice
public class GlobalResponseWrapper implements ResponseBodyAdvice {
    // Wrapping 대상
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true; // 모두
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // body가 이미 CommonResponse
        if (body instanceof CommonResponse) {
            //여기서 걸려버리는 듯
            return body;
        }
        //swagger 관련은 wrapping 안함
        String path = request.getURI().getPath();
        if (path.contains("swagger")
                || path.contains("api-docs")
                || path.contains("webjars")) {
            return body;
        }

        int status = ((ServletServerHttpResponse) response)
                .getServletResponse()
                .getStatus();


        //ServiceCode serviceCode = (status >= 200 && status < 300) ? ServiceCode.SUCCESS : ServiceCode.NOT_DEFINED_ERROR_FROM_WRAPPER; 세분화 하겠습니다


        ServiceCode serviceCode;
        if (status >= 200 && status < 300) {
            serviceCode = ServiceCode.SUCCESS;
        } else if (status == 404) {
            serviceCode = ServiceCode.NOT_FOUND;
        } else if (status == 403) {
            serviceCode = ServiceCode.FORBIDDEN;
        } else if (status == 401) {
            serviceCode = ServiceCode.UNAUTHORIZED;
        } else if (status >= 500) {
            serviceCode = ServiceCode.INTERNAL_SERVER_ERROR;
        } else {
            serviceCode = ServiceCode.NOT_DEFINED_ERROR_FROM_WRAPPER;
        }


        // CommonResponse 생성
        CommonResponse<Object> commonResponse;
        if (serviceCode.equals(ServiceCode.SUCCESS)) {
            commonResponse = CommonResponse.success(body);
        } else {
            commonResponse = CommonResponse.failure(body);   // 원래 Exception은 여기까지 안옴: 전역예외 핸들러에서 CommonResponse로 변환 되었어야했음.
        }


        // case1: body가 String인 경우에는 String으로 재변환 필요
        if (body instanceof String) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                // JSON 문자열 형태로 변환 → String 타입으로 반환해야 converter 충돌이 없음
                return objectMapper.writeValueAsString(commonResponse);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("String response conversion error", e);
            }
        }

        // case2: 그 외는 CommonResponse 그대로 반환
        return commonResponse;
    }

}
 */

@Order(2)
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalResponseWrapper implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true; // 모든 응답에 적용
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        String path = request.getURI().getPath();
        if (path.contains("swagger") || path.contains("api-docs") || path.contains("webjars")) {
            return body;
        }

        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = servletResponse.getStatus();
        ServiceCode serviceCode = resolveServiceCode(status);

        // 이미 CommonResponse인 경우라도 ServiceCode 갱신
        if (body instanceof CommonResponse<?> existing) {
            if (serviceCode == ServiceCode.SUCCESS) {
                return CommonResponse.success(existing.getData());
            } else {
                return CommonResponse.failure(existing.getData(), serviceCode);
            }
        }

        CommonResponse<Object> wrappedResponse = (serviceCode == ServiceCode.SUCCESS)
                ? CommonResponse.success(body)
                : CommonResponse.failure(body, serviceCode);

        // String 처리
        if (body instanceof String) {
            try {
                return objectMapper.writeValueAsString(wrappedResponse);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("String response conversion error", e);
            }
        }

        return wrappedResponse;
    }

    private ServiceCode resolveServiceCode(int status) {
        if (status >= 200 && status < 300) return ServiceCode.SUCCESS;
        return switch (status) {
            case 400 -> ServiceCode.BAD_REQUEST;
            case 401 -> ServiceCode.UNAUTHORIZED;
            case 403 -> ServiceCode.FORBIDDEN;
            case 404 -> ServiceCode.NOT_FOUND;
            case 500 -> ServiceCode.INTERNAL_SERVER_ERROR;
            default -> ServiceCode.NOT_DEFINED_ERROR_FROM_WRAPPER;
        };
    }
}

