package org.hanihome.hanihomebe.security.auth.web.dto;

import lombok.Getter;

@Getter
public class LoginRequestDTO {
    private String email;
    private String password;
}
