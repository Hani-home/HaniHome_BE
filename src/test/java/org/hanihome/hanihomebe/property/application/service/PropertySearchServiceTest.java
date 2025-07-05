package org.hanihome.hanihomebe.property.application.service;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.domain.Role;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.hanihome.hanihomebe.property.domain.TimeSlot;
import org.hanihome.hanihomebe.property.domain.enums.*;
import org.hanihome.hanihomebe.property.web.dto.PropertySearchConditionDTO;
import org.hanihome.hanihomebe.property.web.dto.RentPropertyCreateRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.SharePropertyCreateRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.response.PropertyResponseDTO;
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
import java.util.Map;

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
        Long memberId = memberRepository.save(Member.createFrom("alpha", "gno123", Role.GUEST)).getId();
        /*
         * 매물 종류 / 매물 유형 / 예산 범위 / 입주 가능일 / 지하철역
         *
         */
        Region region = new Region("Australia", "2067", "NSW", "Chatswood", "Smith St", "25", "1203", "Chatswood Central Apartments", BigDecimal.valueOf(0), BigDecimal.valueOf(0));
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

        SharePropertyCreateRequestDTO share_masterRoom = new SharePropertyCreateRequestDTO(
                memberId, // memberId
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
        SharePropertyCreateRequestDTO share_secondRoom = new SharePropertyCreateRequestDTO(
                memberId, // memberId
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
                SharePropertySubType.SECOND_ROOM,
                10.0, // internalArea
                50.0, // totalArea
                3,
                2,
                5,
                2,
                CapacityShare.DOUBLE
        );

        RentPropertyCreateRequestDTO rentDto_house = new RentPropertyCreateRequestDTO(
                memberId, // memberId
                PropertySuperType.RENT,
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
                // Rent 전용 필드
                RentPropertySubType.HOUSE,
                RealEstateType.INDIVIDUAL,
                CapacityRent.FOUR,
                Exposure.SOUTHERN
        );

        RentPropertyCreateRequestDTO rentDto_unit = new RentPropertyCreateRequestDTO(
                memberId, // memberId
                PropertySuperType.RENT,
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
                // Rent 전용 필드
                RentPropertySubType.UNIT,
                RealEstateType.INDIVIDUAL,
                CapacityRent.FOUR,
                Exposure.SOUTHERN
        );

        propertyService.createProperty(share_masterRoom);
        propertyService.createProperty(share_secondRoom);
        propertyService.createProperty(rentDto_house);
        propertyService.createProperty(rentDto_unit);

    }
    /// 매물 종류
    @Test
    void shouldReturnTwoShare_whenFilterByPropertySuperTypeSHARE() {
        Map<PropertySuperType, List<PropertyResponseDTO>> results = propertySearchService.search(
                PropertySearchConditionDTO
                        .builder()
                        .kinds(List.of(PropertySuperType.SHARE))
                        .build());

        logging(results);
        assertEquals(2, results.get(PropertySuperType.SHARE).size());
    }

    @Test
    void shouldReturnTwoRent_whenFilterByPropertySuperTypeRENT() {
        Map<PropertySuperType, List<PropertyResponseDTO>> results = (Map<PropertySuperType, List<PropertyResponseDTO>>) propertySearchService.search(
                PropertySearchConditionDTO
                        .builder()
                        .kinds(List.of(PropertySuperType.RENT))
                        .build()
        );
        logging(results);

        assertEquals(2, results.get(PropertySuperType.RENT).size());
    }

    /// 매물 유형
    @Test
    void shouldReturnOneMaster_whenFilterBySHARE_MASTER_ROOM() {
        Map<PropertySuperType, List<PropertyResponseDTO>> results = (Map<PropertySuperType, List<PropertyResponseDTO>>) propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .kinds(List.of(PropertySuperType.SHARE))
                        .sharePropertySubTypes(List.of(SharePropertySubType.MASTER_ROOM))
                        .build()
        );

        logging(results);
        assertEquals(1, results.get(PropertySuperType.SHARE).size());
    }



    @Test
    void shouldReturnOneUnit_whenFilterByRENT_UNIT() {
        Map<PropertySuperType, List<PropertyResponseDTO>> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .kinds(List.of(PropertySuperType.RENT))
                        .rentPropertySubTypes(List.of(RentPropertySubType.UNIT))
                        .build()
        );
        logging(results);
        assertEquals(1, results.get(PropertySuperType.RENT).size());
    }

    /// 예산 범위
    @Test
    void shouldReturnZeroProperty_whenFilterByWeeklyCost_lowerValue() {
        Map<PropertySuperType, List<PropertyResponseDTO>> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .minWeeklyCost(new BigDecimal(50.00))
                        .maxWeeklyCost(new BigDecimal(99.00))
                        .build()
        );
        logging(results);
          results.forEach((PropertySuperType kind, List<PropertyResponseDTO> result) -> {
            assertTrue(result.isEmpty());
        });
    }


    @Test
    void shouldReturnFourProperty_whenFilterByWeeklyCost_fitValue() {
        Map<PropertySuperType, List<PropertyResponseDTO>> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .minWeeklyCost(new BigDecimal(50.00))
                        .maxWeeklyCost(new BigDecimal(100.00))
                        .build()
        );
        logging(results);
        Map<PropertySuperType, List<PropertyResponseDTO>> results2 = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .minWeeklyCost(new BigDecimal(100.00))
                        .maxWeeklyCost(new BigDecimal(150.00))
                        .build()
        );
        logging(results2);
        Map<PropertySuperType, List<PropertyResponseDTO>> results3 = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .minWeeklyCost(new BigDecimal(50.00))
                        .maxWeeklyCost(new BigDecimal(150.00))
                        .build()
        );
        logging(results3);

        assertEquals(4, results.get(PropertySuperType.RENT).size() + (results.get(PropertySuperType.SHARE).size()));
        assertEquals(4, results2.get(PropertySuperType.RENT).size() + (results.get(PropertySuperType.SHARE).size()));
        assertEquals(4, results3.get(PropertySuperType.RENT).size() + (results.get(PropertySuperType.SHARE).size()));

    }

    @Test
    void shouldReturnZeroProperty_whenFilterByWeeklyCost_upperValue() {
        Map<PropertySuperType, List<PropertyResponseDTO>> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .minWeeklyCost(new BigDecimal(100.01))
                        .maxWeeklyCost(new BigDecimal(200.00))
                        .build()
        );
        logging(results);
        results.forEach((PropertySuperType kind, List<PropertyResponseDTO> result) -> {
            assertTrue(result.isEmpty());
        });
    }

    /// 입주 가능일

    @Test
    void shouldReturnFourProperty_whenFilterByAvailableFrom_fitCriticalValue() {
        Map<PropertySuperType, List<PropertyResponseDTO>> results = propertySearchService.search(
                PropertySearchConditionDTO
                        .builder()
                        .availableFrom(LocalDateTime.of(2025, 5, 1, 5, 5))
                        .availableTo(LocalDateTime.of(2025, 5, 5, 5, 5))
                        .build()
        );
        logging(results);
        assertEquals(4, results.get(PropertySuperType.RENT).size() + (results.get(PropertySuperType.SHARE).size()));

        Map<PropertySuperType, List<PropertyResponseDTO>> results2 = propertySearchService.search(
                PropertySearchConditionDTO
                        .builder()
                        .availableFrom(LocalDateTime.of(2025, 5, 10, 5, 5))
                        .availableTo(LocalDateTime.of(2025, 5, 20, 5, 5))
                        .build()
        );
        logging(results2);
        assertEquals(4, results2.get(PropertySuperType.RENT).size() + (results2.get(PropertySuperType.SHARE).size()));

        Map<PropertySuperType, List<PropertyResponseDTO>> results3 = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .availableFrom(LocalDateTime.of(2025, 5, 1, 5, 5))
                        .availableTo(LocalDateTime.of(2025, 5, 15, 5, 5))
                        .build()
        );
        logging(results3);
        assertEquals(4, results3.get(PropertySuperType.RENT).size() + (results3.get(PropertySuperType.SHARE).size()));
    }

    @Test
    void shouldReturnZeroProperty_whenFilterByAvailableFrom_lowerValue() {
        Map<PropertySuperType, List<PropertyResponseDTO>> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .availableFrom(LocalDateTime.of(2025, 5, 1, 5, 5))
                        .availableTo(LocalDateTime.of(2025, 5, 5, 4, 4))
                        .build()
        );
        logging(results);
        results.forEach((PropertySuperType kind, List<PropertyResponseDTO> result) -> {
            assertTrue(result.isEmpty());
        });

    }

    @Test
    void shouldReturnOneProperty_whenFilterByAvailableFrom() {

        Long memberId = memberRepository.findAll().stream().findFirst().get().getId();
        Region region = new Region("Australia", "2067", "NSW", "Chatswood", "Smith St", "25", "1203", "Chatswood Central Apartments", BigDecimal.valueOf(150), BigDecimal.valueOf(0));

        List<String> photoUrls = List.of("https://example.com/1.jpg", "https://example.com/2.jpg");
        BigDecimal deposit = new BigDecimal("800.00");
        BigDecimal keyDeposit = new BigDecimal("100.00");

        LocalDateTime availableFrom = LocalDateTime.of(2025, 5, 15, 5, 5);
        LocalDateTime availableTo = LocalDateTime.of(2025, 5, 25, 5, 5);

        BigDecimal weeklyCost = new BigDecimal("100.00");
        List<TimeSlot> timeSlots = List.of(new TimeSlot(LocalTime.of(12, 0), LocalTime.of(12, 30)), new TimeSlot(LocalTime.of(15, 0), LocalTime.of(15, 30)));
        LocalDate meetingDateFrom = LocalDate.of(2025, 5, 10);
        LocalDate meetingDateTo = LocalDate.of(2025, 5, 12);

        SharePropertyCreateRequestDTO share_masterRoom = new SharePropertyCreateRequestDTO(
                memberId, // memberId
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
        propertyService.createProperty(share_masterRoom);

        Map<PropertySuperType, List<PropertyResponseDTO>> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .availableFrom(LocalDateTime.of(2025, 5, 11, 5, 5))
                        .availableTo(LocalDateTime.of(2025, 5, 16, 5, 5))
                        .build()
        );
        logging(results);
        assertEquals(1, results.get(PropertySuperType.SHARE).size());
    }
    @Test
    void shouldReturnFourProperty_whenFIlterByAvailableFrom() {

        Long memberId = memberRepository.findAll().stream().findFirst().get().getId();
        Region region = new Region("Australia", "2067", "NSW", "Chatswood", "Smith St", "25", "1203", "Chatswood Central Apartments", BigDecimal.valueOf(150), BigDecimal.valueOf(0));

        List<String> photoUrls = List.of("https://example.com/1.jpg", "https://example.com/2.jpg");
        BigDecimal deposit = new BigDecimal("800.00");
        BigDecimal keyDeposit = new BigDecimal("100.00");

        LocalDateTime availableFrom = LocalDateTime.of(2025, 5, 15, 5, 5);
        LocalDateTime availableTo = LocalDateTime.of(2025, 5, 25, 5, 5);

        BigDecimal weeklyCost = new BigDecimal("100.00");
        List<TimeSlot> timeSlots = List.of(new TimeSlot(LocalTime.of(12, 0), LocalTime.of(12, 30)), new TimeSlot(LocalTime.of(15, 0), LocalTime.of(15, 30)));
        LocalDate meetingDateFrom = LocalDate.of(2025, 5, 10);
        LocalDate meetingDateTo = LocalDate.of(2025, 5, 12);

        SharePropertyCreateRequestDTO share_masterRoom = new SharePropertyCreateRequestDTO(
                memberId, // memberId
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
        propertyService.createProperty(share_masterRoom);

        Map<PropertySuperType, List<PropertyResponseDTO>> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .availableFrom(LocalDateTime.of(2025, 5, 1, 5, 5))
                        .availableTo(LocalDateTime.of(2025, 5, 14, 5, 5))
                        .build()
        );
        logging(results);
        assertEquals(4, results.get(PropertySuperType.SHARE).size()+(results.get(PropertySuperType.RENT).size()));
    }

    @Test
    void shouldReturnZeroProperty_whenFilterByBillIncluded_false() {
        Map<PropertySuperType, List<PropertyResponseDTO>> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .billIncluded(false)
                        .build()
        );
        logging(results);
        results.forEach((PropertySuperType kind, List<PropertyResponseDTO> result) -> {
            assertTrue(result.isEmpty());
        });
    }
    @Test
    void shouldReturnFourProperty_whenFilterByBillIncluded_true() {
        Map<PropertySuperType, List<PropertyResponseDTO>> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .billIncluded(true)
                        .build()
        );
        logging(results);
        assertEquals(4, results.get(PropertySuperType.RENT).size() + (results.get(PropertySuperType.SHARE).size()));
    }
    @Test
    void shouldReturnZeroProperty_whenFilterByNegotiable_false() {
        Map<PropertySuperType, List<PropertyResponseDTO>> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .negotiable(false)
                        .build()
        );
        logging(results);
        results.forEach((PropertySuperType kind, List<PropertyResponseDTO> result) -> {
            assertTrue(result.isEmpty());
        });
    }
    @Test
    void shouldReturnZeroProperty_whenFilterByImmediate_false() {
        Map<PropertySuperType, List<PropertyResponseDTO>> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .immediate(false)
                        .build()
        );
        logging(results);
        results.forEach((PropertySuperType kind, List<PropertyResponseDTO> result) -> {
            assertTrue(result.isEmpty());
        });
    }

    @Test
    void shouldReturnZeroProperty_whenFilterByDistantMetroStop() {
        Region region = new Region("Australia", "2067", "NSW", "Chatswood", "Smith St", "25", "1203", "Chatswood Central Apartments", BigDecimal.valueOf(90.0000), BigDecimal.valueOf(0.0000));
        BigDecimal metroStopLongitude = BigDecimal.valueOf(90.0000);
        BigDecimal metroStopLatitude = BigDecimal.valueOf(0.0000);
        BigDecimal radiusKm = BigDecimal.valueOf(100);

        Map<PropertySuperType, List<PropertyResponseDTO>> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .metroStopLongitude(metroStopLongitude)
                        .metroStopLatitude(metroStopLatitude)
                        .radiusKm(radiusKm)
                        .build()
        );

        logging(results);
        results.forEach((PropertySuperType kind, List<PropertyResponseDTO> result) -> {
            assertTrue(result.isEmpty());
        });
    }

    @Test
    void shouldReturnFourProperty_whenFilterByExactMetroStop() {
        Region region = new Region("Australia", "2067", "NSW", "Chatswood", "Smith St", "25", "1203", "Chatswood Central Apartments", BigDecimal.valueOf(90.0000), BigDecimal.valueOf(0.0000));
        BigDecimal metroStopLongitude = BigDecimal.valueOf(0.0000);
        BigDecimal metroStopLatitude = BigDecimal.valueOf(0.0000);
        BigDecimal radiusKm = BigDecimal.valueOf(100);

        Map<PropertySuperType, List<PropertyResponseDTO>> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .metroStopLongitude(metroStopLongitude)
                        .metroStopLatitude(metroStopLatitude)
                        .radiusKm(radiusKm)
                        .build()
        );

        logging(results);
        assertEquals(4, results.get(PropertySuperType.RENT).size() + (results.get(PropertySuperType.SHARE).size()));
    }
    @Test
    void shouldReturnFourProperty_whenFilterByNearMetroStop() {
        // 경도 1도 : 111.32km
        Region region = new Region("Australia", "2067", "NSW", "Chatswood", "Smith St", "25", "1203", "Chatswood Central Apartments", BigDecimal.valueOf(90.0000), BigDecimal.valueOf(0.0000));
        BigDecimal metroStopLongitude = BigDecimal.valueOf(1.0000);
        BigDecimal metroStopLatitude = BigDecimal.valueOf(0.0000);
        BigDecimal radiusKm = BigDecimal.valueOf(115);

        Map<PropertySuperType, List<PropertyResponseDTO>> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .metroStopLongitude(metroStopLongitude)
                        .metroStopLatitude(metroStopLatitude)
                        .radiusKm(radiusKm)
                        .build()
        );

        logging(results);
        assertEquals(4, results.get(PropertySuperType.RENT).size() + (results.get(PropertySuperType.SHARE).size()));
    }
    @Test
    void shouldReturnTwoProperty_whenFilterByMetroStop_andKinds() {
        // 경도 1도 : 111.32km
        Region region = new Region("Australia", "2067", "NSW", "Chatswood", "Smith St", "25", "1203", "Chatswood Central Apartments", BigDecimal.valueOf(90.0000), BigDecimal.valueOf(0.0000));
        BigDecimal metroStopLongitude = BigDecimal.valueOf(1.0000);
        BigDecimal metroStopLatitude = BigDecimal.valueOf(0.0000);
        BigDecimal radiusKm = BigDecimal.valueOf(115);

        Map<PropertySuperType, List<PropertyResponseDTO>> results = propertySearchService.search(
                PropertySearchConditionDTO.builder()
                        .kinds(List.of(PropertySuperType.SHARE))
                        .metroStopLongitude(metroStopLongitude)
                        .metroStopLatitude(metroStopLatitude)
                        .radiusKm(radiusKm)
                        .build()
        );

        logging(results);
        assertEquals(2, results.get(PropertySuperType.RENT).size() + (results.get(PropertySuperType.SHARE).size()));
    }



    private static void logging(Map<PropertySuperType, List<PropertyResponseDTO>> results) {
        log.info("results: {}", results);
        results.forEach((k, v) -> {
            log.info("k: {}", k);
            log.info("v: {}", v.stream().map(dto -> dto.id()).toList());
        });
    }
}