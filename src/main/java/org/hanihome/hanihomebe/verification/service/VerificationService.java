package org.hanihome.hanihomebe.verification.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.hanihome.hanihomebe.verification.domain.Verification;
import org.hanihome.hanihomebe.verification.domain.VerificationType;
import org.hanihome.hanihomebe.verification.repository.VerificationRepository;
import org.hanihome.hanihomebe.verification.web.converter.VerificationConverter;
import org.hanihome.hanihomebe.verification.web.dto.VerificationRequestDTO;
import org.hanihome.hanihomebe.verification.web.dto.VerificationResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final VerificationRepository verificationRepository;
    private final MemberRepository memberRepository;

    //Create. 사용자 신원인증 요청
    @Transactional
    public VerificationResponseDTO requestVerification(VerificationRequestDTO verificationRequestDTO, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        VerificationType type = verificationRequestDTO.getType();
        String documentImageUrl = verificationRequestDTO.getDocumentImageUrl();

        Verification verification = Verification.createRequestFrom(member, type, documentImageUrl);

        Verification saved = verificationRepository.save(verification);

        return VerificationConverter.toVerificationResponseDTO(saved);
    }

    //Read. 사용자 본인 신원인증 불러오기
    @Transactional(readOnly = true)
    public List<VerificationResponseDTO> getMyVerifications(Long memberId) {
        List<Verification> verifications = verificationRepository.findAllByMemberId(memberId);
        return VerificationConverter.toVerificationResponseDTOList(verifications);
    }

    //Read. 관리자 모든 신원인증 요청 불러오기
    @Transactional(readOnly = true)
    public List<VerificationResponseDTO> getAllVerificationsForAdmin() {
        List<Verification> verifications = verificationRepository.findAll();
        return VerificationConverter.toVerificationResponseDTOList(verifications);
    }

    //Update. 관리자가 신원 인증 승인 or 반려
    @Transactional
    public void approveVerification(Long verificationId) {
        Verification verification = verificationRepository.findById(verificationId)
                .orElseThrow(() -> new IllegalArgumentException("인증 요청이 존재하지 않습니다."));

        verification.approve();
    }

    @Transactional
    public void rejectVerification(String reason, Long verificationId) {
        Verification verification = verificationRepository.findById(verificationId)
                .orElseThrow(() -> new IllegalArgumentException("인증 요청이 존재하지 않습니다."));

        verification.reject(reason);
    }
}
