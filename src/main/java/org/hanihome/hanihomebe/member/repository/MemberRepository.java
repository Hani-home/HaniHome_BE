package org.hanihome.hanihomebe.member.repository;

import org.hanihome.hanihomebe.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 소셜 로그인 시 사용자 식별용
    Optional<Member> findByEmail(String email);

    // ID로 멤버 조회
    Optional<Member> findById(Long memberId);

    //회원가입 시 존재하는 유저인지 확인을 위해 사용
    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}


