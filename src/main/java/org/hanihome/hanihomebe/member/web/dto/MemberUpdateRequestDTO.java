package org.hanihome.hanihomebe.member.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class MemberUpdateRequestDTO {
    private String name;
    private String nickname;
    private LocalDate birthDate;
    private String phoneNumber;
    private String gender;
    private String profileImage;
}

/*
TODO
닉네임 길이 제한 협의 필요
전화번호 형식 협의 필요
 */
