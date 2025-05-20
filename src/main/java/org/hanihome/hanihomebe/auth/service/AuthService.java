package org.hanihome.hanihomebe.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.auth.dto.LoginResponseDTO;
import org.hanihome.hanihomebe.auth.token.RefreshToken;
import org.hanihome.hanihomebe.auth.token.RefreshTokenRepository;
import org.hanihome.hanihomebe.auth.util.GoogleOAuthUtils;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.hanihome.hanihomebe.auth.util.JwtUtils;
import org.hanihome.hanihomebe.member.domain.Role;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {


    @Value("${GOOGLE_CLIENT_ID}")
    private String clientId;

    @Value("${GOOGLE_CLIENT_SECRET}")
    private String clientSecret;

    @Value("${GOOGLE_REDIRECT_URI}")
    private String redirectUri;



    private final GoogleOAuthUtils googleOAuthUtils;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;


    @Transactional
    public LoginResponseDTO googleCodeLogin(String code) {

        /*
        전반적인 로직 :
            프론트가 code를 받아서 해당 api로 보냄.
            백은 id_token을 얻기 위해 그 코드를 받아서 구글에 다시 요청을 보냄.
            id_token을 바탕으로 유저 정보를 가져옴
            이미 우리 DB에 존재하는 유저인지 없는 유저인지 확인하고 없는 유저면 멤버 생성
            유저 정보 바탕으로 토큰 생성
            결과 프론트에 전달
         */



        //HTTP 요청을 보내기 위한 Spring class
        RestTemplate restTemplate = new RestTemplate();

        //요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 구글에 넘길 파라미터 설정
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("redirect_uri", redirectUri);
        params.put("grant_type", "authorization_code");

        //파라미터를 "key=value&key2=value2" 형태의 문자열로 만듦
        StringBuilder body = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (body.length() > 0) body.append("&");
            body.append(entry.getKey()).append("=").append(entry.getValue());
        }


        //HTTP 요청 생성(본문 + 헤더)
        HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);

        //구글에 요청보내서 토큰 받아오기
        ResponseEntity<Map> response = restTemplate.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST,
                request,
                Map.class
        );


        //토큰 못 가져 오면 예외 발생
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("구글 토큰 교환 실패");
        }

        //토큰 받아온 response에서 id_token 추출
        Map<String, Object> responseBody = response.getBody();
        String idToken = (String) responseBody.get("id_token");
        // String accessToken = (String) responseBody.get("access_token"); // 필요하면 사용

        //id_token 검증 후 payload 추출
        GoogleIdToken.Payload payload = googleOAuthUtils.verify(idToken);
        if (payload == null) {
            throw new IllegalArgumentException("유효하지 않은 토큰");
        }

        //payload에서 필요한 이메일, 구글 고유 ID 추출
        String email = payload.getEmail();
        String googleId = payload.getSubject();

        // 회원 조회 (Optional 사용)
        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        boolean isNewUser;
        Member member;


        //기존 유저면 로그인, 없으면 회원가입
        if (optionalMember.isPresent()) {
            member = optionalMember.get();
            isNewUser = false; // 기존 유저
        } else {
            member = Member.builder()
                    .email(email)
                    .password("GOOGLE")
                    .socialProvider("Google")
                    .googleId(googleId)
                    .role(Role.GUEST)
                    .build();
            memberRepository.save(member);
            isNewUser = true; // 신규 유저
        }

        //유저 정보 바탕으로 액세스 토큰, 리프레시 토큰 발급
        String accessToken = jwtUtils.generateAccessToken(member.getId(), member.getRole().name());
        String refreshToken = jwtUtils.generateRefreshToken(member.getId());

        //리프레시 토큰은 redis에 저장
        RefreshToken tokenEntity = RefreshToken.builder()
                .token(refreshToken)
                .memberId(member.getId())
                .build();
        refreshTokenRepository.save(tokenEntity);

        // 토큰이랑 신규유저여부 담아서 프론트에 전달
        return new LoginResponseDTO(accessToken, refreshToken, isNewUser);
    }
}
