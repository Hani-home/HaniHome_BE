package org.hanihome.hanihomebe.auth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.auth.token.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void deleteByToken(String token) {
        refreshTokenRepository.deleteById(token);
    }
}
