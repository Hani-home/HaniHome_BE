package org.hanihome.hanihomebe.auth.token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByMemberId(Long memberId);

    void deleteByMemberId(Long memberId);

    boolean existsByToken(String token);

}
