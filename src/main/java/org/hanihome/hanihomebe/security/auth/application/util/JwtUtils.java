package org.hanihome.hanihomebe.security.auth.application.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.security.auth.user.detail.CustomUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private final long ACCESS_TOKEN_EXPIRATION_MS;
    private final long REFRESH_TOKEN_EXPIRATION_MS;

    private final Key key;

    public JwtUtils(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.access-expiration}") long accessTokenExp,
            @Value("${jwt.refresh-expiration}") long refreshTokenExp
    ){
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.ACCESS_TOKEN_EXPIRATION_MS = accessTokenExp;
        this.REFRESH_TOKEN_EXPIRATION_MS = refreshTokenExp;

    }

    public String generateAccessToken(Long userId, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_MS);
        System.out.println(expiryDate);
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_MS);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    /*
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }
     */

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            return false;  // 만료는 false로 처리
        } catch (MalformedJwtException e) {
            throw new CustomException(ServiceCode.MALFORMED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new CustomException(ServiceCode.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new CustomException(ServiceCode.INVALID_TOKEN);
        } catch (JwtException e) {
            throw new CustomException(ServiceCode.INVALID_TOKEN);  // 기타 JWT 에러 fallback
        }
    }



    public Long getUserIdFromToken(String token) {
        return Long.parseLong(getClaims(token).getSubject());
    }

    public String getRoleFromToken(String token) {
        return getClaims(token).get("role", String.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public long getRemainingValidity(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration().getTime() - System.currentTimeMillis();
    }

    public String extractAccessTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        System.out.println(header);
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    public String extractRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    //현재 인증된 사용자의 정보를 담는 객체 => SecurityContextHolder에 담아 필요할 때마다 추출하여 사용
    /*
    Authentication이 가지고 있는 정보
    1. Principal: 유저의 식별 정보 (여기선 userId와 role을 CustomUserDetails에 담아 넘김)
    2. Credentials: 비밀번호 같은 민감 정보 => 여기선 필요없을 것 같아 null로 넘김
    3. Authorities: 권한 (GUEST,HOST)
    4. 인증 여부
     */
    public Authentication getAuthentication(String accessToken) {
        Long userId = Long.valueOf(getClaims(accessToken).getSubject());
        String role = getClaims(accessToken).get("role", String.class);

        CustomUserDetails userDetails = new CustomUserDetails(userId, role, null);

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }


}
