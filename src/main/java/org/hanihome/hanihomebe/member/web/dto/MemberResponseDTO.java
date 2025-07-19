package org.hanihome.hanihomebe.member.web.dto;

import org.hanihome.hanihomebe.member.domain.Gender;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.verification.web.dto.VerificationSummaryDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MemberResponseDTO {
    Long getId();
    String getEmail();
    String getName();
    String getNickname();
    String getProfileImage();
    LocalDate getBirthDate();
    String getPhoneNumber();
    Gender getGender();
    LocalDateTime getCreatedAt();
    boolean isVerifiedUser();
    List<VerificationSummaryDTO> getVerifications();

}
