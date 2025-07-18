package org.hanihome.hanihomebe.member.service;


import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.member.domain.Consent;
import org.hanihome.hanihomebe.member.domain.ConsentType;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.domain.Role;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.hanihome.hanihomebe.member.web.dto.ConsentAgreementDTO;
import org.hanihome.hanihomebe.member.web.dto.MemberCompleteProfileRequestDTO;
import org.hanihome.hanihomebe.member.web.dto.MemberNicknameCheckResponseDTO;
import org.hanihome.hanihomebe.member.web.dto.MemberDetailResponseDTO;
import org.hanihome.hanihomebe.member.web.dto.MemberResponseDTO;
import org.hanihome.hanihomebe.member.web.dto.MemberSignupRequestDTO;
import org.hanihome.hanihomebe.member.web.dto.MemberUpdateRequestDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //테스트 유저 생성을 위한 회원가입, 로그인 => 추후 일반 유저 확장 시에도 사용가능


    public void signup(MemberSignupRequestDTO memberSignupRequestDTO) {
        if(memberRepository.existsByEmail(memberSignupRequestDTO.getEmail())) {
            throw new CustomException(ServiceCode.EMAIL_ALREADY_EXISTS);
        }
        Member member = Member.createFrom(
                memberSignupRequestDTO.getEmail(),
                passwordEncoder.encode(memberSignupRequestDTO.getPassword()),
                Role.GUEST
        );
        memberRepository.save(member);
    }


    public void completeProfile(Long memberId, MemberCompleteProfileRequestDTO dto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));

        if (member.isRegistered()) {
            throw new CustomException(ServiceCode.MEMBER_ALREADY_REGISTERED);
        }

        //필수 동의 항목 체크했는지 확인
        validateRequiredConsents(dto.getConsents());

        for (ConsentAgreementDTO consentDto : dto.getConsents()) {
            Consent consent = Consent.create(member, consentDto.getType(), consentDto.isAgreed());
            member.getConsents().add(consent); // 연관관계 설정 (cascade로 저장됨)
        }

        // 기본 프로필 정보 설정
        member.completeProfile(dto);

        // 등록 상태 표시
        member.markAsRegistered();

    }


    /*
    <T> 제네릭을 사용하겠다고 명시
    Function<Member, T> converter : member Type을 받고 T를 반환하는 함수(함수형 인터페이스) => apply는 인터페이스에 정의된 메서드
     */
    @Transactional(readOnly = true)
    public <T extends MemberResponseDTO> T getMemberDTOById(
            Long memberId, Function<Member, T> converter
    ) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));
        return converter.apply(member);
    }



    public void updateMember(Long memberId, MemberUpdateRequestDTO memberUpdateRequestDTO) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));

        //멤버도메인에 위임
        member.updateMember(memberUpdateRequestDTO);
    }


    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));

        memberRepository.delete(member);
    }

    public MemberNicknameCheckResponseDTO checkNickname(String nickname) {
        boolean available = !memberRepository.existsByNickname(nickname);
        return new MemberNicknameCheckResponseDTO(available);
    }

    private void validateRequiredConsents(List<ConsentAgreementDTO> consents) {
        //필수인 것만 추출
        List<ConsentType> requiredConsents = Arrays.stream(ConsentType.values())
                .filter(ConsentType::isRequired)
                .toList();


        for (ConsentType required : requiredConsents) {
            boolean agreed = consents.stream()
                    .filter(c -> c.getType() == required) //DTO엔 담긴 애의 type이 required인 애만 추출
                    .map(ConsentAgreementDTO::isAgreed) //dto에서 동의 여부를 꺼냄
                    .findFirst()
                    .orElse(false);

            if (!agreed) {
                throw new CustomException(ServiceCode.REQUIRED_CONSENT_MISSING);
            }
        }


    }
}
