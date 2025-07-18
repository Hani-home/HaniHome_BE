package org.hanihome.hanihomebe.property.application.service;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.transaction.Transactional;
import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.member.domain.Gender;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.domain.Role;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.hanihome.hanihomebe.metro.domain.MetroStop;
import org.hanihome.hanihomebe.metro.repository.MetroStopRepository;
import org.hanihome.hanihomebe.property.domain.enums.*;
import org.hanihome.hanihomebe.item.domain.CategoryCode;
import org.hanihome.hanihomebe.item.domain.OptionCategory;
import org.hanihome.hanihomebe.item.domain.OptionItem;
import org.hanihome.hanihomebe.item.repository.OptionCategoryRepository;
import org.hanihome.hanihomebe.item.repository.OptionItemRepository;
import org.hanihome.hanihomebe.property.domain.vo.*;
import org.hanihome.hanihomebe.property.web.dto.request.SharePropertyCreateRequestDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class PropertyServiceTest {
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private OptionItemRepository optionItemRepository;
    @Autowired
    private OptionCategoryRepository optionCategoryRepository;
    @Autowired
    private MetroStopRepository metroStopRepository;

    Long memberId;

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

        OptionCategory category = OptionCategory.create(CategoryCode.PROPERTY_CAT4);
        optionCategoryRepository.save(category);
        optionItemRepository.save(OptionItem.createDefault(category, "item1"));
        optionItemRepository.save(OptionItem.createDefault(category, "item2"));
        optionItemRepository.save(OptionItem.createDefault(category, "item3"));
        memberId = memberRepository.save(Member.builder()
                .email("test@hanihome.com")
                .password("<PASSWORD>")
                .birthDate(LocalDate.now())
                .gender(Gender.MALE)
                .role(Role.GUEST)
                .phoneNumber("01012341234")
                .name("olaf")
                .nickname("olaf" + "Nickname")
                .build()).getId();
    }

    @Test
    void base() {
        System.out.println("hallo");
    }
    @Test
    void createProperty() {
        //given
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
                        ParkingOption.STREET_PARKING,
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
        //when
        Long id = propertyService.createProperty(dto).id();
        //then
        Assertions.assertEquals(memberId,propertyService.getPropertyById(id).memberId());

    }
}