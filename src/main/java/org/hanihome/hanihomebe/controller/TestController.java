package org.hanihome.hanihomebe.controller;


import org.hanihome.hanihomebe.auth.dto.LoginResponseDTO;
import org.hanihome.hanihomebe.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    private final AuthService authService;

    public TestController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("서버 정상 작동 중");
    }

    @GetMapping("/v1/admin/signin")
    public LoginResponseDTO signin() {
        return authService.adminLogin();
    }
}
