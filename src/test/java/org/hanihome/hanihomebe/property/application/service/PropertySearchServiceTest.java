package org.hanihome.hanihomebe.property.application.service;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.domain.Role;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.hanihome.hanihomebe.metro.domain.MetroStop;
import org.hanihome.hanihomebe.metro.repository.MetroStopRepository;
import org.hanihome.hanihomebe.property.domain.enums.*;
import org.hanihome.hanihomebe.property.domain.vo.*;
import org.hanihome.hanihomebe.property.web.dto.enums.PropertyViewType;
import org.hanihome.hanihomebe.property.web.dto.request.PropertySearchConditionDTO;
import org.hanihome.hanihomebe.property.web.dto.request.create.RentPropertyCreateRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.request.create.SharePropertyCreateRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.response.summary.PropertySummaryDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/* 함수 네이밍
 * should[ExpectedResult]_when[Condition]_[AndAdditionalConditions]
 */
@Slf4j
@ActiveProfiles("test")
@Transactional
@SpringBootTest
class PropertySearchServiceTest {
    @Autowired
    PropertySearchService propertySearchService;
    @Autowired
    PropertyService propertyService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MetroStopRepository metroStopRepository;

    @BeforeAll
    static void setupEnv() {
        Dotenv env = Dotenv.configure()
                .filename(".env.test")
                .ignoreIfMissing()
                .load();
        env.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });
    }

    @BeforeEach
    void init() {
        Long memberId = memberRepository.save(
                Member.createFrom("alpha", "gno123", Role.GUEST)
        ).getId();

        MetroStop parentStop = MetroStop.createParent(
                "200030",                            // businessStopId
                "Martin Place Station",              // stopName
                new BigDecimal("37.5665"),           // stopLatitude
                new BigDecimal("126.9780"),          // stopLongitude
                "1",                                 // locationType (e.g., 1 = parent)
                true,                                // wheelchairBoarding
                null                                 // platformCode (null for parent)
        );

        metroStopRepository.save(parentStop);

        // 공통 객체 설정
        Region region = new Region(
                "Australia", "2067", "NSW", "Chatswood",
                "Smith St", "25", "1203",
                "Chatswood Central Apartments",
                BigDecimal.ZERO, BigDecimal.ZERO
        );
        List<String> photoUrls = List.of(
                "https://example.com/1.jpg",
                "https://example.com/2.jpg"
        );
        CostDetails costDetails = CostDetails.builder()
                .weeklyCost(new BigDecimal("100.00"))
                .deposit(new BigDecimal("800.00"))
                .keyDeposit(new BigDecimal("100.00"))
                .isBillIncluded(true)
                .costDescription("전기세, 수도세 포함")
                .build();
        LivingConditions livingConditions = new LivingConditions(
                4, 12, "6개월 이상 계약 가능", true
        );
        MoveInInfo moveInInfo = new MoveInInfo(
                LocalDateTime.of(2025,5,5,5,5),
                LocalDateTime.of(2025,5,10,5,5),
                true,
                true
        );
        List<TimeSlot> timeSlots = List.of(
                new TimeSlot(LocalTime.of(12,0), LocalTime.of(12,30)),
                new TimeSlot(LocalTime.of(15,0), LocalTime.of(15,30))
        );
        LocalDate meetingDateFrom = LocalDate.of(2025,5,10);
        LocalDate meetingDateTo   = LocalDate.of(2025,5,12);

        // Share Master Room 매물 생성
        SharePropertyCreateRequestDTO share_masterRoom =
                new SharePropertyCreateRequestDTO(
                        memberId,
                        PropertySuperType.SHARE,
                        GenderPreference.ANY,
                        true,                     // lgbtAvailable
                        region,
                        photoUrls,
                        costDetails,
                        List.of(1L,2L,3L),        // optionItemIds
                        livingConditions,
                        moveInInfo,
                        meetingDateFrom,
                        meetingDateTo,
                        timeSlots,
                        null,                     // viewingAvailableDateTimes
                        true,                     // viewingAlwaysAvailable
                        "깨끗하고 조용한 마스터룸입니다.",
                        SharePropertySubType.MASTER_ROOM,
                        new ShareInternalDetails(
                                10.0, 50.0,
                                3, 2, 5, 2
                        ),                      // internalDetails
                        CapacityShare.DOUBLE       // capacityShare
                );

        // Share Second Room 매물 생성
        SharePropertyCreateRequestDTO share_secondRoom =
                new SharePropertyCreateRequestDTO(
                        memberId,
                        PropertySuperType.SHARE,
                        GenderPreference.ANY,
                        true,
                        region,
                        photoUrls,
                        costDetails,
                        List.of(1L,2L,3L),
                        livingConditions,
                        moveInInfo,
                        meetingDateFrom,
                        meetingDateTo,
                        timeSlots,
                        null,
                        false,
                        "깨끗하고 조용한 세컨드룸입니다.",
                        SharePropertySubType.SECOND_ROOM,
                        new ShareInternalDetails(
                                2.0, 2.0,
                                2, 2, 2, 2
                        ),
                        CapacityShare.DOUBLE
                );

        // Rent House 매물 생성
        RentPropertyCreateRequestDTO rent_house =
                new RentPropertyCreateRequestDTO(
                        memberId,
                        PropertySuperType.RENT,
                        GenderPreference.ANY,
                        true,
                        region,
                        photoUrls,
                        costDetails,
                        List.of(1L,2L,3L),
                        livingConditions,
                        moveInInfo,
                        meetingDateFrom,
                        meetingDateTo,
                        timeSlots,
                        null,
                        true,
                        "역세권 깔끔한 하우스입니다.",
                        RentPropertySubType.HOUSE,
                        new RentInternalDetails(
                                45.0, 60.0,
                                3, 1,
                                10, 3
                        ),
                        CapacityRent.FOUR
                );

        // Rent Unit 매물 생성
        RentPropertyCreateRequestDTO rent_unit =
                new RentPropertyCreateRequestDTO(
                        memberId,
                        PropertySuperType.RENT,
                        GenderPreference.ANY,
                        true,
                        region,
                        photoUrls,
                        costDetails,
                        List.of(1L,2L,3L),
                        livingConditions,
                        moveInInfo,
                        meetingDateFrom,
                        meetingDateTo,
                        timeSlots,
                        null,
                        true,
                        "깔끔한 유닛 매물입니다.",
                        RentPropertySubType.UNIT,
                        new RentInternalDetails(
                                30.0, 50.0,
                                2, 1,
                                5, 2
                        ),
                        CapacityRent.FOUR
                );

        propertyService.createProperty(share_masterRoom);
        propertyService.createProperty(share_secondRoom);
        propertyService.createProperty(rent_house);
        propertyService.createProperty(rent_unit);
    }
    /// 매물 종류
    @Test
    void shouldReturnTwoShare_whenFilterByPropertySuperTypeSHARE() {
        List<PropertySummaryDTO> results = propertySearchService.search(
                PropertySearchConditionDTO
                        .builder()
                        .kinds(List.of(PropertySuperType.SHARE))
                        .build(), PropertyViewType.SUMMARY);

        logging(results);
        assertEquals(2, results.size());
    }

    @Test
    void shouldReturnTwoRent_whenFilterByPropertySuperTypeRENT() {
        List<PropertySummaryDTO> results = propertySearchService.search(
                PropertySearchConditionDTO
                        .builder()
                        .kinds(List.of(PropertySuperType.RENT))
                        .build(), PropertyViewType.SUMMARY
        );
        logging(results);

        assertEquals(2, results.size());
    }

    /// 매물 유형
    @Test
    void shouldReturnOneMaster_whenFilterBySHARE_MASTER_ROOM() {
        List<PropertySummaryDTO> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .kinds(List.of(PropertySuperType.SHARE))
                        .sharePropertySubTypes(List.of(SharePropertySubType.MASTER_ROOM))
                        .build()
                , PropertyViewType.SUMMARY);

        logging(results);
        assertEquals(1, results.size());
    }

    @Test
    void shouldReturnOneUnit_whenFilterByRENT_UNIT() {
        List<PropertySummaryDTO> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .kinds(List.of(PropertySuperType.RENT))
                        .rentPropertySubTypes(List.of(RentPropertySubType.UNIT))
                        .build()
                , PropertyViewType.SUMMARY );
        logging(results);
        assertEquals(1, results.size());

    }

    /// 예산 범위
    @Test
    void shouldReturnZeroProperty_whenFilterByWeeklyCost_lowerValue() {
        List<PropertySummaryDTO> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .minWeeklyCost(new BigDecimal(50.00))
                        .maxWeeklyCost(new BigDecimal(99.00))
                        .build()
                , PropertyViewType.SUMMARY);
        logging(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void shouldReturnFourProperty_whenFilterByWeeklyCost_fitValue() {
        List<PropertySummaryDTO> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .minWeeklyCost(new BigDecimal(50.00))
                        .maxWeeklyCost(new BigDecimal(100.00))
                        .build()
                , PropertyViewType.SUMMARY);
        logging(results);

        List<PropertySummaryDTO> results2 = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .minWeeklyCost(new BigDecimal(100.00))
                        .maxWeeklyCost(new BigDecimal(150.00))
                        .build()
                , PropertyViewType.SUMMARY);
        logging(results2);

        List<PropertySummaryDTO> results3 = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .minWeeklyCost(new BigDecimal(50.00))
                        .maxWeeklyCost(new BigDecimal(150.00))
                        .build()
                , PropertyViewType.SUMMARY);
        logging(results3);

        assertEquals(4, results.size());
        assertEquals(4, results2.size());
        assertEquals(4, results3.size());
    }

    @Test
    void shouldReturnZeroProperty_whenFilterByWeeklyCost_upperValue() {
        List<PropertySummaryDTO> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .minWeeklyCost(new BigDecimal(100.01))
                        .maxWeeklyCost(new BigDecimal(200.00))
                        .build()
                , PropertyViewType.SUMMARY);
        logging(results);
        assertTrue(results.isEmpty());
    }

    /// 입주 가능일
    @Test
    void shouldReturnFourProperty_whenFilterByAvailableFrom_fitCriticalValue() {
        List<PropertySummaryDTO> results = propertySearchService.search(
                PropertySearchConditionDTO
                        .builder()
                        .availableFrom(LocalDateTime.of(2025, 5, 1, 5, 5))
                        .availableTo(LocalDateTime.of(2025, 5, 5, 5, 5))
                        .build()
                , PropertyViewType.SUMMARY);
        logging(results);
        assertEquals(4, results.size());

        List<PropertySummaryDTO> results2 = propertySearchService.search(
                PropertySearchConditionDTO
                        .builder()
                        .availableFrom(LocalDateTime.of(2025, 5, 10, 5, 5))
                        .availableTo(LocalDateTime.of(2025, 5, 20, 5, 5))
                        .build()
                , PropertyViewType.SUMMARY);
        logging(results2);
        assertEquals(4, results2.size());

        List<PropertySummaryDTO> results3 = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .availableFrom(LocalDateTime.of(2025, 5, 1, 5, 5))
                        .availableTo(LocalDateTime.of(2025, 5, 15, 5, 5))
                        .build()
                , PropertyViewType.SUMMARY);
        logging(results3);
        assertEquals(4, results3.size());
    }

    @Test
    void shouldReturnZeroProperty_whenFilterByAvailableFrom_lowerValue() {
        List<PropertySummaryDTO> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .availableFrom(LocalDateTime.of(2025, 5, 1, 5, 5))
                        .availableTo(LocalDateTime.of(2025, 5, 5, 4, 4))
                        .build()
                , PropertyViewType.SUMMARY);
        logging(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void shouldReturnOneProperty_whenFilterByAvailableFrom() {
        //given
        // 공통 객체 설정
        Long memberId = memberRepository.findAll().stream().findFirst().orElseThrow().getId();
        Region region = new Region(
                "Australia", "2067", "NSW", "Chatswood",
                "Smith St", "25", "1203",
                "Chatswood Central Apartments",
                BigDecimal.ZERO, BigDecimal.ZERO
        );
        List<String> photoUrls = List.of(
                "https://example.com/1.jpg",
                "https://example.com/2.jpg"
        );
        CostDetails costDetails = CostDetails.builder()
                .weeklyCost(new BigDecimal("100.00"))
                .deposit(new BigDecimal("800.00"))
                .keyDeposit(new BigDecimal("100.00"))
                .isBillIncluded(true)
                .costDescription("전기세, 수도세 포함")
                .build();
        LivingConditions livingConditions = new LivingConditions(
                4, 12, "6개월 이상 계약 가능", true
        );
        MoveInInfo moveInInfo = new MoveInInfo(
                LocalDateTime.of(2025,5,15,5,5),
                LocalDateTime.of(2025,5,25,5,5),
                true,
                false
        );
        List<TimeSlot> timeSlots = List.of(
                new TimeSlot(LocalTime.of(12,0), LocalTime.of(12,30)),
                new TimeSlot(LocalTime.of(15,0), LocalTime.of(15,30))
        );
        LocalDate meetingDateFrom = LocalDate.of(2025,5,10);
        LocalDate meetingDateTo   = LocalDate.of(2025,5,12);

        // Share Master Room 매물 생성
        SharePropertyCreateRequestDTO dto =
                new SharePropertyCreateRequestDTO(
                        memberId,
                        PropertySuperType.SHARE,
                        GenderPreference.ANY,
                        true,                     // lgbtAvailable
                        region,
                        photoUrls,
                        costDetails,
                        List.of(1L,2L,3L),        // optionItemIds
                        livingConditions,
                        moveInInfo,
                        meetingDateFrom,
                        meetingDateTo,
                        timeSlots,
                        null,                     // viewingAvailableDateTimes
                        true,                     // viewingAlwaysAvailable
                        "깨끗하고 조용한 마스터룸입니다.",
                        SharePropertySubType.MASTER_ROOM,
                        new ShareInternalDetails(
                                10.0, 50.0,
                                3, 2, 5, 2
                        ),                      // internalDetails
                        CapacityShare.DOUBLE       // capacityShare
                );
        propertyService.createProperty(dto);
        List<PropertySummaryDTO> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .availableFrom(LocalDateTime.of(2025, 5, 11, 5, 5))
                        .availableTo(LocalDateTime.of(2025, 5, 16, 5, 5))
                        .build()
                , PropertyViewType.SUMMARY);
        logging(results);
        assertEquals(1, results.size());
    }

    @Test
    void shouldReturnFourProperty_whenFilterByAvailableFrom() {
        // 코드 유지...

        List<PropertySummaryDTO> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .availableFrom(LocalDateTime.of(2025, 5, 1, 5, 5))
                        .availableTo(LocalDateTime.of(2025, 5, 14, 5, 5))
                        .build()
                , PropertyViewType.SUMMARY);
        logging(results);
        assertEquals(4, results.size());
    }

    /// bill included
    @Test
    void shouldReturnZeroProperty_whenFilterByBillIncluded_false() {
        List<PropertySummaryDTO> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .billIncluded(false)
                        .build()
                , PropertyViewType.SUMMARY);
        logging(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void shouldReturnFourProperty_whenFilterByBillIncluded_true() {
        List<PropertySummaryDTO> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .billIncluded(true)
                        .build()
                , PropertyViewType.SUMMARY);
        logging(results);
        assertEquals(4, results.size());
    }

    /// negotiable
    @Test
    void shouldReturnZeroProperty_whenFilterByNegotiable_false() {
        List<PropertySummaryDTO> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .negotiable(false)
                        .build()
                , PropertyViewType.SUMMARY);
        logging(results);
        assertTrue(results.isEmpty());
    }

    /// immediate
    @Test
    void shouldReturnZeroProperty_whenFilterByImmediate_false() {
        List<PropertySummaryDTO> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .immediate(false)
                        .build()
                , PropertyViewType.SUMMARY);
        logging(results);
        assertTrue(results.isEmpty());
    }

    /// 지하철역 Nkm
    @Test
    void shouldReturnZeroProperty_whenFilterByDistantMetroStop() {
        BigDecimal metroStopLongitude = BigDecimal.valueOf(90.0000);
        BigDecimal metroStopLatitude = BigDecimal.valueOf(0.0000);
        BigDecimal radiusKm = BigDecimal.valueOf(100);

        List<PropertySummaryDTO> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .metroStopLongitude(metroStopLongitude)
                        .metroStopLatitude(metroStopLatitude)
                        .radiusKm(radiusKm)
                        .build()
                , PropertyViewType.SUMMARY);

        logging(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void shouldReturnFourProperty_whenFilterByExactMetroStop() {
        BigDecimal metroStopLongitude = BigDecimal.valueOf(0.0000);
        BigDecimal metroStopLatitude = BigDecimal.valueOf(0.0000);
        BigDecimal radiusKm = BigDecimal.valueOf(100);

        List<PropertySummaryDTO> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .metroStopLongitude(metroStopLongitude)
                        .metroStopLatitude(metroStopLatitude)
                        .radiusKm(radiusKm)
                        .build()
                , PropertyViewType.SUMMARY);

        logging(results);
        assertEquals(4, results.size());
    }

    @Test
    void shouldReturnFourProperty_whenFilterByNearMetroStop() {
        // 경도 1도 : 111.32km
        BigDecimal metroStopLongitude = BigDecimal.valueOf(1.0000);
        BigDecimal metroStopLatitude = BigDecimal.valueOf(0.0000);
        BigDecimal radiusKm = BigDecimal.valueOf(115);

        List<PropertySummaryDTO> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .metroStopLongitude(metroStopLongitude)
                        .metroStopLatitude(metroStopLatitude)
                        .radiusKm(radiusKm)
                        .build()
                , PropertyViewType.SUMMARY);

        logging(results);
        assertEquals(4, results.size());
    }

    @Test
    void shouldReturnTwoProperty_whenFilterByMetroStop_andKinds() {
        // 경도 1도 : 111.32km
        BigDecimal metroStopLongitude = BigDecimal.valueOf(1.0000);
        BigDecimal metroStopLatitude = BigDecimal.valueOf(0.0000);
        BigDecimal radiusKm = BigDecimal.valueOf(115);

        List<PropertySummaryDTO> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .kinds(List.of(PropertySuperType.SHARE))
                        .metroStopLongitude(metroStopLongitude)
                        .metroStopLatitude(metroStopLatitude)
                        .radiusKm(radiusKm)
                        .build()
                , PropertyViewType.SUMMARY);

        logging(results);
        assertEquals(2, results.size());
    }

    @Test
    void shouldReturnFourProperty_whenFilterBySuburb() {
        List<PropertySummaryDTO> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .suburb("Chatswood")
                        .build()
                , PropertyViewType.SUMMARY);

        assertEquals(4, results.size());
    }

    @Test
    void shouldReturnFourProperty_whenFilterBySuburb_andLowerCase() {
        List<PropertySummaryDTO> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .suburb("chatswood")
                        .build()
                , PropertyViewType.SUMMARY);

        assertEquals(4, results.size());
    }

    @Test
    void shouldReturnZeroProperty_whenFilterBySuburb() {
        List<PropertySummaryDTO> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .suburb("doowchats")
                        .build()
                , PropertyViewType.SUMMARY);

        assertEquals(0, results.size());
    }

    @Test
    void shouldReturnTwoProperty_whenFilterByMetroStop_andKinds_andSuburb() {
        // 경도 1도 : 111.32km
        BigDecimal metroStopLongitude = BigDecimal.valueOf(1.0000);
        BigDecimal metroStopLatitude = BigDecimal.valueOf(0.0000);
        BigDecimal radiusKm = BigDecimal.valueOf(115);

        List<PropertySummaryDTO> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .kinds(List.of(PropertySuperType.SHARE))
                        .suburb("Chatswood")
                        .metroStopLongitude(metroStopLongitude)
                        .metroStopLatitude(metroStopLatitude)
                        .radiusKm(radiusKm)
                        .build()
                , PropertyViewType.SUMMARY);

        logging(results);
        assertEquals(2, results.size());
    }

    private static void logging(List<PropertySummaryDTO> results) {
        log.info("results: {}", results);
        log.info("ids: {}", results.stream().map(dto -> dto.id()).toList());
    }
}
