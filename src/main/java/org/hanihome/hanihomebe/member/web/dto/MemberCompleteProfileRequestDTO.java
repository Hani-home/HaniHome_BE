package org.hanihome.hanihomebe.member.web.dto;

import lombok.Getter;
import org.hanihome.hanihomebe.member.domain.Gender;

import java.util.List;

@Getter
public class MemberCompleteProfileRequestDTO {
    private String name;
    private String email;
    private String phoneNumber;
    private String profileImage;

    private String nickname;
    private Gender gender;
    //일단 한개만 백에서 region class 확정나면 수정하겠음.
    private String interestRegion;

    private List<ConsentAgreementDTO> consents;


}
