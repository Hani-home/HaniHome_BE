package org.hanihome.hanihomebe.security.auth.application.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.global.response.dto.CommonResponse;
import org.hanihome.hanihomebe.security.auth.application.jwt.refresh.RefreshToken;
import org.hanihome.hanihomebe.security.auth.application.service.AuthService;
import org.hanihome.hanihomebe.security.auth.application.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Optional;

import static org.hanihome.hanihomebe.global.response.domain.ServiceCode.NOT_DEFINED_ERROR_FROM_FILTER;

//OnceperRequestFilter: 모든 HTTP 요청마다 실행됨
/*
1. Access Token이 유효한지 확인
2. 필요시 Refresh Token 확인 후 Access Token 발급
3. 토큰이 없거나 유효하지 않다면 인증 실패 처리
 */
// TODO: doFilterInternal이 너무 길어서 리팩터링 필요함
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final AuthService authService;
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public JwtAuthenticationFilter(JwtUtils jwtUtils, AuthService authService, RedisTemplate<String, String> redisTemplate) {
        this.jwtUtils = jwtUtils;
        this.authService = authService;
        this.redisTemplate = redisTemplate;
    }

    //이 필터를 거치지 않는 URL 설정
    @Override
    protected  boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String path = request.getRequestURI();

        // Swagger, auth 등 경로 필터 제외
        return path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.equals("/swagger-ui.html")
                || path.startsWith("/api/v1/auth/social/login")
                || path.equals("/health")
                || path.equals("/api/v1/auth/login")
                || path.equals("/api/v1/members/signup");
        // 필요하면 추가

    }



    //실제 요청마다 실행되는 부분
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtUtils.extractAccessTokenFromHeader(request);
        String refreshToken = jwtUtils.extractRefreshTokenFromCookie(request);

        try {
            if (accessToken == null) {
                log.info("Access token is null should passed");
                filterChain.doFilter(request, response);
                return;
            }

            if (jwtUtils.validateToken(accessToken)) {
                log.info("Access token is valid");
                processAccessToken(accessToken, filterChain, request, response);
            } else {
                if (refreshToken != null && jwtUtils.validateToken(refreshToken)) {
                    log.info("Refresh token is valid");
                    String newAccessToken = authService.reissueAccessToken(refreshToken);
                    response.setHeader("Authorization", "Bearer " + newAccessToken);
                    processAccessToken(newAccessToken, filterChain, request, response);
                } else {
                    log.info("Refresh token is expired");
                    throw new CustomException(ServiceCode.ACCESS_TOKEN_EXPIRED);
                } //refresh 토큰이 null인 경우도 고려해야할까
            }
        } catch (CustomException e) {
            log.warn("JWT Filter CustomException occurred: {}", e.getServiceCode().name());
            setErrorResponse(response, e.getServiceCode());
        } catch (Exception e) {
            log.error("Unexpected error in JwtAuthenticationFilter", e);
            setErrorResponse(response, NOT_DEFINED_ERROR_FROM_FILTER);
        }
    }

    private void processAccessToken(String token,
                                    FilterChain filterChain,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws ServletException, IOException {

        String blacklistKey = "BLACKLIST:" + token;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(blacklistKey))) {
            throw new CustomException(ServiceCode.ACCESS_TOKEN_BLACKLISTED);
        }

        Authentication authentication = jwtUtils.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    //TODO: 에러 코드 및 메시지 세분화 예정
    /*
    private void setErrorResponse(HttpServletResponse response, String errorCode, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        ErrorResponse errorResponse = new ErrorResponse(errorCode, message);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

     */
    //mapper NONO
    private void setErrorResponse(HttpServletResponse response, ServiceCode serviceCode) throws IOException {
        response.setStatus(serviceCode.getHttpStatus().value());
        response.setContentType("application/json; charset=utf-8");

        ObjectMapper objectMapper = new ObjectMapper();
        CommonResponse<Object> errorResponse = CommonResponse.failure(serviceCode);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }



}




