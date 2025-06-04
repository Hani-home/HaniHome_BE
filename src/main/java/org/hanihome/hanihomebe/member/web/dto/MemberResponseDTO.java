package org.hanihome.hanihomebe.member.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hanihome.hanihomebe.member.domain.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;

//고민: role은 안 넣어도 되겠지? 지금은 myPage에서 보여주는 회원정보 조회라고 생각해서 role은 안 넣겠음
@Getter
@AllArgsConstructor
public class MemberResponseDTO {
    private Long id;
    private String email;
    private String name;
    private String nickname;
    private String profileImage;
    private LocalDate birthDate;
    private String phoneNumber;
    private String gender;
    private LocalDateTime createdAt;

    public static MemberResponseDTO CreateFrom(Member member) {
        return new MemberResponseDTO(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getNickname(),
                member.getProfileImage(),
                member.getBirthDate(),
                member.getPhoneNumber(),
                member.getGender(),
                member.getCreatedAt()
        );
    }


}
