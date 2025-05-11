package org.hanihome.hanihomebe.member.repository;

import org.hanihome.hanihomebe.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 소셜 로그인 시 사용자 식별용
    Optional<Member> findByEmail(String email);
}

