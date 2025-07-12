package org.hanihome.hanihomebe.security.auth.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hanihome.hanihomebe.security.auth.web.dto.GoogleLoginRequestDTO;
import org.hanihome.hanihomebe.security.auth.web.dto.LoginRequestDTO;
import org.hanihome.hanihomebe.security.auth.web.dto.LoginResponseDTO;
import org.hanihome.hanihomebe.security.auth.application.service.AuthService;
import org.hanihome.hanihomebe.security.auth.application.jwt.blacklist.BlacklistService;
import org.hanihome.hanihomebe.security.auth.application.jwt.refresh.RefreshTokenService;
import org.hanihome.hanihomebe.security.auth.application.util.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final BlacklistService blacklistService;
    private final JwtUtils jwtUtils;



    @PostMapping("/social/login")
    public ResponseEntity<LoginResponseDTO> googleCodeLogin(@RequestBody GoogleLoginRequestDTO dto, HttpServletRequest request) {
        LoginResponseDTO tokens = authService.googleCodeLogin(dto.getCode(), request);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", tokens.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .domain("hanihome.app")
                .maxAge(Duration.ofDays(7))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getAccessToken())
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(tokens);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(name = "refreshToken", required = false) String refreshToken, @RequestHeader("Authorization") String authHeader, HttpServletResponse response) {
        if(refreshToken != null){
            Long userId = jwtUtils.getUserIdFromToken(refreshToken);
            refreshTokenService.deleteByToken(userId);
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            long expireMs = jwtUtils.getRemainingValidity(token);
            blacklistService.blacklistAccessToken(token, expireMs);
        }

        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok("로그아웃 완료");
    }

    //테스트 유저용(일반 유저) 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO response = authService.login(loginRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtUtils.extractRefreshTokenFromCookie(request);
        log.info("리프레시 토큰 추출");

        if (refreshToken == null || !jwtUtils.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token is missing or invalid");
        }

        try {
            log.info("액세스 토큰 재발급 진입");
            String newAccessToken = authService.reissueAccessToken(refreshToken);
            log.info("재발급 성공");
            response.setHeader("Authorization", "Bearer " + newAccessToken);
            return ResponseEntity.ok("Access token reissued");
        } catch (Exception e) {
            log.error("Access token reissue failed", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token reissue failed");
        }
    }


}
