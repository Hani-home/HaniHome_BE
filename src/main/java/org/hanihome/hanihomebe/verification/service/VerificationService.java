package org.hanihome.hanihomebe.verification.service;

import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
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
@Transactional(readOnly = true)
public class VerificationService {

    private final VerificationRepository verificationRepository;
    private final MemberRepository memberRepository;

    //Create. 사용자 신원인증 요청
    @Transactional
    public VerificationResponseDTO requestVerification(VerificationRequestDTO verificationRequestDTO, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));

        VerificationType type = verificationRequestDTO.getType();
        List<String> documentImageUrls = verificationRequestDTO.getDocumentImageUrls();

        Verification verification = Verification.createRequestFrom(member, type, documentImageUrls);

        Verification saved = verificationRepository.save(verification);

        return VerificationConverter.toVerificationResponseDTO(saved);
    }

    //Read. 사용자 본인 신원인증 불러오기
    public List<VerificationResponseDTO> getMyAllVerifications(Long memberId) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));

        List<Verification> verifications = verificationRepository.findAllByMemberId(memberId);
        return VerificationConverter.toVerificationResponseDTOList(verifications);
    }

    //Read. 사용자 본인 신원인증 요청 개별로 불러오기
    public VerificationResponseDTO getMyVerification(Long memberId, Long verificationId) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));

        Verification verification = verificationRepository.findByIdAndMemberId(verificationId, memberId)
                .orElseThrow(() -> new CustomException(ServiceCode.VERIFICATION_NOT_EXISTS));

        return VerificationConverter.toVerificationResponseDTO(verification);
    }

    //Read. 관리자 모든 신원인증 요청 불러오기
    public List<VerificationResponseDTO> getAllVerificationsForAdmin() {
        List<Verification> verifications = verificationRepository.findAll();
        return VerificationConverter.toVerificationResponseDTOList(verifications);
    }

    public VerificationResponseDTO getVerificationById(Long verificationId) {
        Verification findVerification = verificationRepository.findById(verificationId)
                .orElseThrow(() -> new CustomException(ServiceCode.VERIFICATION_NOT_EXISTS));

        return VerificationConverter.toVerificationResponseDTO(findVerification);
    }

    //Update. 관리자가 신원 인증 승인 or 반려
    @Transactional
    public VerificationResponseDTO approveVerification(Long verificationId) {
        Verification verification = verificationRepository.findById(verificationId)
                .orElseThrow(() -> new CustomException(ServiceCode.VERIFICATION_NOT_EXISTS));

        verification.approve();

        return VerificationConverter.toVerificationResponseDTO(verification);
    }

    @Transactional
    public VerificationResponseDTO rejectVerification(String reason, Long verificationId) {
        Verification verification = verificationRepository.findById(verificationId)
                .orElseThrow(() -> new CustomException(ServiceCode.VERIFICATION_NOT_EXISTS));

        verification.reject(reason);

        return VerificationConverter.toVerificationResponseDTO(verification);
    }
}
