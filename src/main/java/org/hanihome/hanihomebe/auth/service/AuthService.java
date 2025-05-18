package org.hanihome.hanihomebe.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.auth.token.RefreshToken;
import org.hanihome.hanihomebe.auth.token.RefreshTokenRepository;
import org.hanihome.hanihomebe.auth.util.GoogleOAuthUtils;
import org.hanihome.hanihomebe.auth.dto.LoginResponseDTO;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.hanihome.hanihomebe.auth.util.JwtUtils;
import org.hanihome.hanihomebe.member.domain.Role;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final GoogleOAuthUtils googleOAuthUtils;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;

    @Transactional
    public LoginResponseDTO googleLogin(String idToken) {
        GoogleIdToken.Payload payload = googleOAuthUtils.verify(idToken);
        if (payload == null) {
            throw new IllegalArgumentException("유효하지 않은 토큰");
        }

        String email = payload.getEmail();
        String googleId = payload.getSubject();

        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> {
                    Member newMember = Member.builder()
                            .email(email)
                            .password("GOOGLE")
                            .socialProvider("Google")
                            .googleId(googleId)
                            .role(Role.GUEST)
                            .build();
                    return memberRepository.save(newMember);
                });

        String accessToken = jwtUtils.generateAccessToken(member.getId(), member.getRole().name());
        String refreshToken = jwtUtils.generateRefreshToken(member.getId());

        RefreshToken tokenEntity = RefreshToken.builder()
                .token(refreshToken)
                .memberId(member.getId())
                .build();
        refreshTokenRepository.save(tokenEntity);

        return new LoginResponseDTO(accessToken, refreshToken);
    }

}
