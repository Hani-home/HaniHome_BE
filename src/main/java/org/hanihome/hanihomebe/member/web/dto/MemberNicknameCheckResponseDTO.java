package org.hanihome.hanihomebe.member.web.dto;

import lombok.Getter;

@Getter
public class MemberNicknameCheckResponseDTO {
    private boolean available;

    public MemberNicknameCheckResponseDTO(boolean available) {
        this.available = available;
    }
}
