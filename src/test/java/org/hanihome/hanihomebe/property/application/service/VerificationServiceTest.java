package org.hanihome.hanihomebe.property.application.service;



import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.member.domain.Gender;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.domain.Role;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.hanihome.hanihomebe.verification.domain.Verification;
import org.hanihome.hanihomebe.verification.domain.VerificationType;
import org.hanihome.hanihomebe.verification.repository.VerificationRepository;
import org.hanihome.hanihomebe.verification.service.VerificationService;
import org.hanihome.hanihomebe.verification.web.dto.VerificationRequestDTO;
import org.hanihome.hanihomebe.verification.web.dto.VerificationResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class VerificationServiceTest {

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private VerificationRepository verificationRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Long memberId;
    private Long verificationId;
    private Long adminId;

    @BeforeEach
    void init() {
        Member member = Member.builder()
                .email("verify@hanihome.com")
                .password("password")
                .birthDate(LocalDate.of(1999, 1, 1))
                .gender(Gender.FEMALE)
                .role(Role.GUEST)
                .name("테스트유저")
                .nickname("테스트닉")
                .phoneNumber("01012345678")
                .build();

        memberId = memberRepository.save(member).getId();

        //지금은 권한 분리를 적용 안 해서 쓰이지는 않음.
        Member admin = Member.builder()
                .email("admin@hanihome.com")
                .password("password")
                .birthDate(LocalDate.of(1999, 1, 1))
                .gender(Gender.MALE)
                .role(Role.ADMIN)
                .name("관리자")
                .nickname("관리자에용")
                .phoneNumber("01012345678")
                .build();

        adminId = memberRepository.save(admin).getId();
        /*
        //테스트용 신원인증 요청 생성
        Verification verification = Verification.createRequestFrom(
                member,
                VerificationType.ID_CARD,
                List.of("https://img1.com", "https://img2.com")
        );
        verificationId = verificationRepository.save(verification).getId();
         */
    }

    @Test
    @DisplayName("신원 인증 요청 시 올바른 요청일 때 작동확인")
    void requestVerificationTest() {
        // given
        VerificationRequestDTO requestDTO = new VerificationRequestDTO(
                VerificationType.ID_CARD,
                List.of("https://example.com/image1.jpg", "https://example.com/image2.jpg")
        );

        List<String> expectedUrls = List.of("https://example.com/image1.jpg", "https://example.com/image2.jpg");

        // when
        VerificationResponseDTO responseDTO = verificationService.requestVerification(requestDTO, memberId);

        // then
        assertNotNull(responseDTO);
        assertEquals(VerificationType.ID_CARD, responseDTO.getType());
        assertEquals("PENDING", responseDTO.getStatus().name());
        assertEquals(expectedUrls, responseDTO.getDocumentImageUrls());
        assertEquals(2, responseDTO.getDocumentImageUrls().size());
    }

    @Test
    @DisplayName("존재하지 않는 멤버 ID로 요청 시 예외 발생")
    void requestVerificationTest_shouldThrowException_whenMemberNotFound() {
        Long nonExistMemberId = 999L; //L은 뭐지
        VerificationRequestDTO requestDTO = new VerificationRequestDTO(
                VerificationType.ID_CARD,
                List.of("https://example.com/id1.jpg", "https://example.com/id2.jpg")
        );

        CustomException exception = assertThrows(CustomException.class, () -> {
            verificationService.requestVerification(requestDTO, nonExistMemberId);
        });

        assertEquals(ServiceCode.MEMBER_NOT_EXISTS, exception.getServiceCode());
    }

    @Test
    @DisplayName("정상 요청일 때 자신 모든 신원인증 리스트 불어오기")
    void getMyAllVerificationsTest() {

        Member member = memberRepository.findById(memberId).get();

        Verification verification = Verification.createRequestFrom(
                member,
                VerificationType.ID_CARD,
                List.of("https://img1.com", "https://img2.com")
        );
        verificationId = verificationRepository.save(verification).getId();

        List<VerificationResponseDTO> result = verificationService.getMyAllVerifications(memberId);

        assertEquals(1, result.size());
        VerificationResponseDTO dto = result.get(0); //일단 첫번째 것만 확인

        assertEquals(VerificationType.ID_CARD, dto.getType());
        assertEquals("PENDING", dto.getStatus().name());
        assertEquals(List.of("https://img1.com", "https://img2.com"), dto.getDocumentImageUrls());
        assertNotNull(dto.getRequestedAt());
    }

    @Test
    @DisplayName("정상 요청일 때 자신의 개별 신원인증 불러오기")
    void getMyVerificationTest() {
        Member member = memberRepository.findById(memberId).get();

        Verification verification = Verification.createRequestFrom(
                member,
                VerificationType.ID_CARD,
                List.of("https://img1.com", "https://img2.com")
        );

        verificationId = verificationRepository.save(verification).getId();

        VerificationResponseDTO result = verificationService.getMyVerification(memberId, verificationId);

        assertEquals(VerificationType.ID_CARD, result.getType());
        assertEquals("PENDING", result.getStatus().name());
        assertEquals(List.of("https://img1.com", "https://img2.com"), result.getDocumentImageUrls());
        assertNotNull(result.getRequestedAt());
    }
    /* 이 친구는 권한 분리하고 나서 생각해보겠습니다.
    @Test
    @DisplayName("정상 요청일 때 관리자가 모든 신원인증 요청 불러오기")
    void getAllVerificationsForAdminTest() {
        List<VerificationResponseDTO> result = verificationService.getAllVerificationsForAdmin();

    }
     */





}
