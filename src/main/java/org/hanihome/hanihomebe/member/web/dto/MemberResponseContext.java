package org.hanihome.hanihomebe.member.web.dto;

import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.verification.web.dto.VerificationSummaryDTO;

import java.util.List;
// Service의 Function<MemberResponseContext, T> converter 부분에서 여기 인자를 하나만 넣을 수 있다길래 이렇게 한 번에 묶어서 보내줍니다.
public record MemberResponseContext (
        Member member,
        List<VerificationSummaryDTO> verificationSummaries,
        boolean isVerifiedUser
) {}
