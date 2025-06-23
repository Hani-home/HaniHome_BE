package org.hanihome.hanihomebe.verification.repository;

import org.hanihome.hanihomebe.verification.domain.Verification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VerificationRepository extends JpaRepository<Verification, Long> {
    List<Verification> findAllByMemberId(Long memberId);
    Optional<Verification> findByIdAndMemberId(Long id, Long memberId);

}
