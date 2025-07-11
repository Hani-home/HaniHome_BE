package org.hanihome.hanihomebe.property.application.service;


import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.member.domain.Gender;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.domain.Role;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.hanihome.hanihomebe.property.domain.ShareProperty;
import org.hanihome.hanihomebe.property.domain.TimeSlot;
import org.hanihome.hanihomebe.property.domain.enums.CapacityShare;
import org.hanihome.hanihomebe.property.domain.enums.GenderPreference;
import org.hanihome.hanihomebe.property.domain.enums.ParkingOption;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.property.domain.enums.SharePropertySubType;
import org.hanihome.hanihomebe.property.repository.PropertyRepository;
import org.hanihome.hanihomebe.property.web.dto.SharePropertyCreateRequestDTO;
import org.hanihome.hanihomebe.report.application.domain.ReportTargetType;
import org.hanihome.hanihomebe.report.service.ReportService;
import org.hanihome.hanihomebe.report.web.dto.ReportRequestDTO;
import org.hanihome.hanihomebe.report.web.dto.ReportResponseDTO;
import org.hanihome.hanihomebe.verification.web.dto.VerificationResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@ActiveProfiles
@SpringBootTest
@Transactional
public class ReportServiceTest {

    @Autowired
    ReportService reportService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PropertyRepository propertyRepository;

    private Long reporterId;


    private ReportTargetType targetType = ReportTargetType.PROPERTY;
    private Long targetId;


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

        reporterId = memberRepository.save(member).getId();

        Region region = new Region("Australia", "2067", "NSW", "Chatswood", "Smith St", "25", "1203", "Chatswood Central Apartments", BigDecimal.valueOf(150), BigDecimal.valueOf(0));
        List<String> photoUrls = List.of("https://example.com/1.jpg", "https://example.com/2.jpg");
        BigDecimal deposit = new BigDecimal("800.00");
        BigDecimal keyDeposit = new BigDecimal("100.00");

        LocalDateTime availableFrom = LocalDateTime.of(2025, 5, 5, 5, 5);
        LocalDateTime availableTo = LocalDateTime.of(2025, 5, 10, 5, 5);

        BigDecimal weeklyCost = new BigDecimal("100.00");

        // 뷰잉 가능 날짜
        List<TimeSlot> timeSlots = List.of(new TimeSlot(LocalTime.of(12, 0), LocalTime.of(12, 30)), new TimeSlot(LocalTime.of(15, 0), LocalTime.of(15, 30)));
        LocalDate meetingDateFrom = LocalDate.of(2025, 5, 10);
        LocalDate meetingDateTo = LocalDate.of(2025, 5, 12);

        SharePropertyCreateRequestDTO dto = new SharePropertyCreateRequestDTO(
                reporterId, // memberId
                PropertySuperType.SHARE,
                GenderPreference.ANY,

                region,
                photoUrls,
                weeklyCost,
                true,
                List.of(1L, 2L, 3L), // optionItemIds
                "전기세, 수도세 포함",
                deposit,
                keyDeposit,
                4, // noticePeriodWeeks
                12, // minimumStayWeeks
                "6개월 이상 계약 가능",
                availableFrom,
                availableTo,
                true,
                true,
                ParkingOption.STREET_PARKING,
                meetingDateFrom,
                meetingDateTo,
                timeSlots,
                "깨끗하고 조용한 마스터룸입니다.",
                SharePropertySubType.MASTER_ROOM,
                10.0, // internalArea
                50.0, // totalArea
                3,
                2,
                5,
                2,
                CapacityShare.DOUBLE
        );
        ShareProperty shareProperty = ShareProperty.create(dto, member);
        targetId = propertyRepository.save(shareProperty).getId();

    }


    @Test
    @DisplayName("존재하는 매물에 유효한 유저가 신고 가능")
    void createReportTest() {
        ReportRequestDTO dto = new ReportRequestDTO(
                targetId,
                ReportTargetType.PROPERTY,
                List.of("https://example.com/image1.jpg", "https://example.com/image2.jpg"),
                "가보니 두꺼비집이였습니다..."

        );

        List<String> expectedUrls = List.of("https://example.com/image1.jpg", "https://example.com/image2.jpg");

        ReportResponseDTO responseDTO = reportService.createReport(reporterId, dto);

        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getReportTargetType()).isEqualTo(ReportTargetType.PROPERTY);
        assertThat(responseDTO.getTargetId()).isEqualTo(targetId);
        assertThat(responseDTO.getDescription()).isEqualTo("가보니 두꺼비집이였습니다...");
        assertThat(responseDTO.getDocumentImageUrls()).containsExactlyElementsOf(expectedUrls);
        assertThat(responseDTO.getReportId()).isNotNull();

    }



}
