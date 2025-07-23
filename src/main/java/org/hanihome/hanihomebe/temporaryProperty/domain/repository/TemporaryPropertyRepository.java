package org.hanihome.hanihomebe.temporaryProperty.domain.repository;

import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.temporaryProperty.domain.TemporaryProperty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporaryPropertyRepository extends JpaRepository<TemporaryProperty, Long> {
    //공통 메서드
    int countByMember(Member member);
}
