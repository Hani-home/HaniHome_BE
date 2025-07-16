package org.hanihome.hanihomebe.security.auth.application.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import jakarta.servlet.http.HttpServletRequest;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.hanihome.hanihomebe.security.auth.application.jwt.refresh.RefreshToken;
import org.hanihome.hanihomebe.security.auth.application.jwt.refresh.RefreshTokenRepository;
import org.hanihome.hanihomebe.security.auth.application.util.GoogleOAuthUtils;
import org.hanihome.hanihomebe.security.auth.application.util.JwtUtils;
import org.hanihome.hanihomebe.security.auth.web.dto.LoginRequestDTO;
import org.hanihome.hanihomebe.security.auth.web.dto.LoginResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;


import java.util.Optional;
@Transactional(readOnly = true)
@Service
public class AuthService {


    @Value("${oauth.google.client-id}")
    private String clientId;

    @Value("${oauth.google.client-secret}")
    private String clientSecret;

    @Value("${oauth.google.redirect-uri-local}")
    private String redirectUriLocal;

    @Value("${oauth.google.redirect-uri-prod}")
    private String redirectUriProd;



    private final GoogleOAuthUtils googleOAuthUtils;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            GoogleOAuthUtils googleOAuthUtils,
            MemberRepository memberRepository,
            RefreshTokenRepository refreshTokenRepository,
            JwtUtils jwtUtils,
            @Lazy PasswordEncoder passwordEncoder

    ) {
        this.googleOAuthUtils = googleOAuthUtils;
        this.memberRepository = memberRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public LoginResponseDTO googleCodeLogin(String code, HttpServletRequest request) {
        /*
        전반적인 로직 :
            프론트가 code를 받아서 해당 api로 보냄.
            백은 id_token을 얻기 위해 그 코드를 받아서 구글에 다시 요청을 보냄.
            id_token을 바탕으로 유저 정보를 가져옴
            이미 우리 DB에 존재하는 유저인지 없는 유저인지 확인하고 없는 유저면 멤버 생성
            유저 정보 바탕으로 토큰 생성
            결과 프론트에 전달
         */

        Map<String, Object> responseBody = requestGoogleToken(code, request);

        //토큰 받아온 response에서 id_token 추출
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

        Member member = findOrCreateMember(email, googleId);
        boolean isNewUser = !member.isRegistered();

        //유저 정보 바탕으로 액세스 토큰, 리프레시 토큰 발급
        String accessToken = jwtUtils.generateAccessToken(member.getId(), member.getRole().name());
        String refreshToken = jwtUtils.generateRefreshToken(member.getId());

        //리프레시 토큰은 redis에 저장
        storeRefreshToken(member.getId(), refreshToken);

        // 토큰이랑 신규유저여부 담아서 프론트에 전달
        return new LoginResponseDTO(accessToken, refreshToken, isNewUser);
    }

    private Map<String, Object> requestGoogleToken(String code, HttpServletRequest httpRequest) {
        //HTTP 요청을 보내기 위한 Spring class
        RestTemplate restTemplate = new RestTemplate();

        //요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //Origin 기준으로 로컬인지 배포 환경인지 판단

        String origin = httpRequest.getHeader("Origin");
        String referer = httpRequest.getHeader("Referer");

        String redirectUri;
        if ((origin != null && origin.contains("localhost")) ||
                (referer != null && referer.contains("localhost"))) {
            redirectUri = redirectUriLocal;
        } else {
            redirectUri = redirectUriProd;
        }


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
        return response.getBody();
    }

    private Member findOrCreateMember(String email, String googleId) {
        // 회원 조회 (Optional 사용)
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        Member member;


        //기존 유저면 로그인, 없으면 회원가입
        if (optionalMember.isEmpty()) {
            member = Member.createFromGoogleSignUp(email, googleId);
            memberRepository.save(member);
        } else {
            member = optionalMember.get();
        }
        return member;

    }

    private void storeRefreshToken(Long memberId, String refreshToken) {
        //리프레시 토큰은 redis에 저장
        RefreshToken tokenEntity = RefreshToken.builder()
                .memberId(memberId)
                .token(refreshToken)
                .build();
        refreshTokenRepository.save(tokenEntity);
    }


    //일반 로그인
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        Member member = memberRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), member.getPassword())) {
            throw new CustomException(ServiceCode.PASSWORD_NOT_MATCH);
        }

        //JWT 발급
        String accessToken = jwtUtils.generateAccessToken(member.getId(), member.getRole().name());
        String refreshToken = jwtUtils.generateRefreshToken(member.getId());

        RefreshToken tokenEntity = RefreshToken.builder()
                .memberId(member.getId())
                .token(refreshToken)
                .build();
        refreshTokenRepository.save(tokenEntity);

        return new LoginResponseDTO(accessToken, refreshToken, false);
    }

    public String reissueAccessToken(String refreshToken) {

        if(!jwtUtils.validateToken(refreshToken)) {
            throw new CustomException(ServiceCode.INVALID_REFRESH_TOKEN);
        }

        Long userId = jwtUtils.getUserIdFromToken(refreshToken);

        //한 UserId에 대해 저장된 RefreshToken이 여러 개인 경우도 고려하는 게 나으려나...?
        RefreshToken storedToken = refreshTokenRepository.findByMemberId(userId)
                .orElseThrow(()-> new CustomException(ServiceCode.INVALID_REFRESH_TOKEN));

        if(!storedToken.getToken().equals(refreshToken)) {
            throw new CustomException(ServiceCode.INVALID_REFRESH_TOKEN);
        }

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));

        String role = member.getRole().name();

        return jwtUtils.generateAccessToken(member.getId(), role);
    }
    // 테스트용 로그인 DB에 있는 ID:1 인 사용자를 가지고 토큰발급
    @Transactional
    public LoginResponseDTO adminLogin() {

        Member member = memberRepository.findById(1L).orElseThrow(() -> new RuntimeException("DB에 한명의 Member는 필요합니다"));
        //유저 정보 바탕으로 액세스 토큰, 리프레시 토큰 발급
        String accessToken = jwtUtils.generateAccessToken(member.getId(), member.getRole().name());
        String refreshToken = jwtUtils.generateRefreshToken(member.getId());

        //리프레시 토큰은 redis에 저장
        RefreshToken tokenEntity = RefreshToken.builder()
                .memberId(member.getId())
                .token(refreshToken)
                .build();
        refreshTokenRepository.save(tokenEntity);

        // 토큰이랑 신규유저여부 담아서 프론트에 전달
        return new LoginResponseDTO(accessToken, refreshToken, true);
    }

}
