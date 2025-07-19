package org.hanihome.hanihomebe.member.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hanihome.hanihomebe.member.domain.Gender;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.verification.domain.Verification;
import org.hanihome.hanihomebe.verification.domain.VerificationStatus;
import org.hanihome.hanihomebe.verification.domain.VerificationType;
import org.hanihome.hanihomebe.verification.web.dto.VerificationSummaryDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

//고민: role은 안 넣어도 되겠지? 지금은 myPage에서 보여주는 회원정보 조회라고 생각해서 role은 안 넣겠음
@Getter
@AllArgsConstructor
public class MemberDetailResponseDTO implements MemberResponseDTO {
    private Long id;
    private String email;
    private String name;
    private String nickname;
    private String profileImage;
    private LocalDate birthDate;
    private String phoneNumber;
    private Gender gender;
    private LocalDateTime createdAt;
    private boolean isVerifiedUser;
    private List<VerificationSummaryDTO> verifications;

    //static은 override 불가
    public static MemberDetailResponseDTO CreateFrom(MemberResponseContext context ) {
        /*
        List<Verification> verifications = member.getVerifications();

        List<VerificationSummaryDTO> verificationSummaries = new ArrayList<>();

        for (VerificationType type : VerificationType.values()) {
            //각 타입에 해당하는 인증 목록
            List<Verification> filtered = verifications.stream()
                    .filter(v -> v.getType()==type)
                    .collect(Collectors.toList());

            //가장 최근 요청
            Verification latest = filtered.stream()
                    .max(Comparator.comparing(Verification::getApprovedAt))
                    .orElse(null);

            boolean isVerified = latest !=null && latest.getStatus() == VerificationStatus.APPROVED;

            verificationSummaries.add(new VerificationSummaryDTO(type, isVerified));
        }

        boolean isVerifiedUser = verificationSummaries.stream().anyMatch(VerificationSummaryDTO::isVerified);
         */
        Member member = context.member();
        List<VerificationSummaryDTO> verificationSummaries = context.verificationSummaries();
        boolean isVerifiedUser = context.isVerifiedUser();

        return new MemberDetailResponseDTO(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getNickname(),
                member.getProfileImage(),
                member.getBirthDate(),
                member.getPhoneNumber(),
                member.getGender(),
                member.getCreatedAt(),
                isVerifiedUser,
                verificationSummaries
        );
    }


}
