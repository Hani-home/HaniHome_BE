package org.hanihome.hanihomebe.security.auth.application.jwt.refresh;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void deleteByToken(Long userId) {
        refreshTokenRepository.deleteById(userId);
    }
}
