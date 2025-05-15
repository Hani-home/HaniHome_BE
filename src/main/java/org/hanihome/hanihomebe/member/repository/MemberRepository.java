package org.hanihome.hanihomebe.member.repository;

import org.hanihome.hanihomebe.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 소셜 로그인 시 사용자 식별용
    Optional<Member> findByEmail(String email);
}


