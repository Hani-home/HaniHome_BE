package org.hanihome.hanihomebe.member.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.domain.Role;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.hanihome.hanihomebe.member.web.dto.MemberResponseDTO;
import org.hanihome.hanihomebe.member.web.dto.MemberSignupRequestDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //테스트 유저 생성을 위한 회원가입, 로그인 => 추후 일반 유저 확장 시에도 사용가능

    @Transactional
    public void signup(MemberSignupRequestDTO memberSignupRequestDTO) {
        if(memberRepository.existsByEmail(memberSignupRequestDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        Member member = Member.createFrom(
                memberSignupRequestDTO.getEmail(),
                passwordEncoder.encode(memberSignupRequestDTO.getPassword()),
                Role.GUEST
        );
        memberRepository.save(member);
    }

    public MemberResponseDTO getMemberById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        return MemberResponseDTO.CreateFrom(member);
    }
}
