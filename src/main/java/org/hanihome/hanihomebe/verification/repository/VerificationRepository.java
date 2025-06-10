package org.hanihome.hanihomebe.verification.repository;

import org.hanihome.hanihomebe.verification.domain.Verification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VerificationRepository extends JpaRepository<Verification, Long> {
    List<Verification> findAllByMemberId(Long memberId);

}
