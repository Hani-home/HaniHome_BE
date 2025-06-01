package org.hanihome.hanihomebe.security.auth.application.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hanihome.hanihomebe.security.auth.application.service.AuthService;
import org.hanihome.hanihomebe.security.auth.application.util.JwtUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

//OnceperRequestFilter: 모든 HTTP 요청마다 실행됨
/*
1. Access Token이 유효한지 확인
2. 필요시 Refresh Token 확인 후 Access Token 발급
3. 토큰이 없거나 유효하지 않다면 인증 실패 처리
 */
// TODO: doFilterInternal이 너무 길어서 리팩터링 필요함
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final AuthService authService;
    private final RedisTemplate<String, String> redisTemplate;

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
                || path.startsWith("/api/v1/auth/social/login"); // 필요하면 추가

    }

    //실제 요청마다 실행되는 부분
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtils.extractAccessTokenFromHeader(request);
        String refreshToken = jwtUtils.extractRefreshTokenFromCookie(request);

        try {
            if (accessToken != null) {
                try {
                    if (jwtUtils.validateToken(accessToken)) {
                        // 액세스 토큰이 블랙리스트에 등록되어있는지 확인
                        String blacklistKey = "BLACKLIST:" + accessToken;
                        if (Boolean.TRUE.equals(redisTemplate.hasKey(blacklistKey))) {
                            SecurityContextHolder.clearContext();
                            setErrorResponse(response, "ACCESS_TOKEN_BLACKLISTED", "Access token is blacklisted (로그아웃)");
                            return;
                        }

                        //SecurityContext에 authentication 저장 => 컨트롤러에서 어노테이션을 이용해 인증 정보 사용 가능
                        Authentication authentication = jwtUtils.getAuthentication(accessToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        filterChain.doFilter(request, response);
                        return;
                    }
                } catch (io.jsonwebtoken.ExpiredJwtException e) {
                    // 만료는 refreshToken 분기로 진행 (아래에서 처리)
                } catch (Exception e) {
                    // 유효하지 않은 accessToken이면 바로 인증 실패
                    SecurityContextHolder.clearContext();
                    setErrorResponse(response, "ACCESS_TOKEN_INVALID", "Access token is invalid");
                    return;
                }
            }

            if (refreshToken != null && jwtUtils.validateToken(refreshToken)) {
                //리프레시 토큰 이용해 accessToken 재발급
                String newAccessToken = authService.reissueAccessToken(refreshToken);
                response.setHeader("Authorization", "Bearer " + newAccessToken);

                Authentication authentication = jwtUtils.getAuthentication(newAccessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
                return;
            } else {
                SecurityContextHolder.clearContext();
                setErrorResponse(response, "TOKEN_INVALID_OR_EXPIRED", "Access/Refresh token is missing or expired");
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            setErrorResponse(response, "AUTH_EXCEPTION", "Authentication failed: " + e.getMessage());
        }
    }

    //TODO: 에러 코드 및 메시지 세분화 예정
    private void setErrorResponse(HttpServletResponse response, String errorCode, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        ErrorResponse errorResponse = new ErrorResponse(errorCode, message);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    // 간단한 에러 DTO
    public static class ErrorResponse {
        private String errorCode;
        private String message;

        public ErrorResponse(String errorCode, String message) {
            this.errorCode = errorCode;
            this.message = message;
        }
        public String getErrorCode() { return errorCode; }
        public String getMessage() { return message; }
    }


}




