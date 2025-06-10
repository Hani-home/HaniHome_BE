package org.hanihome.hanihomebe.security.auth.application.jwt.refresh;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash(value = "refreshToken", timeToLive = 86400)
public class RefreshToken {

    @Id
    private Long memberId; //Redis키 리프레시 토큰 재발급할 때 memberId로 찾는 게 더 효율적인 거 같음

    private String token;


    public static RefreshToken of(Long memberId, String token) {
        return RefreshToken.builder()
                .memberId(memberId)
                .token(token)
                .build();
    }




}
