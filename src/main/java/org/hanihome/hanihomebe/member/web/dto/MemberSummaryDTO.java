package org.hanihome.hanihomebe.member.web.dto;

import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.verification.domain.VerificationStatus;

public record MemberSummaryDTO(
        Long id,
        String profileImage,
        String nickname,
        Boolean verified
) {
    public static MemberSummaryDTO from(Member member) {
        boolean isVerified = member.getVerifications()
                .stream()
                .anyMatch(v -> v.getStatus() == VerificationStatus.APPROVED);

        return new MemberSummaryDTO(
                member.getId(),
                member.getProfileImage(),
                member.getNickname(),
                isVerified
        );
    }
}
