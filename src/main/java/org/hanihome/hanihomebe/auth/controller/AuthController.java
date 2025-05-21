package org.hanihome.hanihomebe.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.auth.dto.GoogleLoginRequestDTO;
import org.hanihome.hanihomebe.auth.dto.LoginResponseDTO;
import org.hanihome.hanihomebe.auth.service.AuthService;
import org.hanihome.hanihomebe.auth.service.BlacklistService;
import org.hanihome.hanihomebe.auth.service.RefreshTokenService;
import org.hanihome.hanihomebe.auth.util.JwtUtils;
import org.springframework.http.HttpHeaders;
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
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final BlacklistService blacklistService;
    private final JwtUtils jwtUtils;


    @PostMapping("/social/login")
    public ResponseEntity<LoginResponseDTO> googleCodeLogin(@RequestBody GoogleLoginRequestDTO dto) {
        LoginResponseDTO tokens = authService.googleCodeLogin(dto.getCode());

        ResponseCookie cookie = ResponseCookie.from("refreshToken", tokens.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
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


}
