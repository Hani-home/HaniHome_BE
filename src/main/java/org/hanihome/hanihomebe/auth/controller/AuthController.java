package org.hanihome.hanihomebe.auth.controller;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.auth.dto.GoogleLoginRequestDTO;
import org.hanihome.hanihomebe.auth.dto.LoginResponseDTO;
import org.hanihome.hanihomebe.auth.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/google-login")
    public ResponseEntity<Void> googleLogin(@RequestBody GoogleLoginRequestDTO dto) {
        LoginResponseDTO tokens = authService.googleLogin(dto.getIdToken());

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
                .build();
    }
}
