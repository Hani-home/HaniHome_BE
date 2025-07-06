package org.hanihome.hanihomebe.property.application.service;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.transaction.Transactional;
import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.member.domain.Gender;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.domain.Role;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.hanihome.hanihomebe.property.domain.TimeSlot;
import org.hanihome.hanihomebe.property.domain.enums.*;
import org.hanihome.hanihomebe.item.domain.CategoryCode;
import org.hanihome.hanihomebe.item.domain.OptionCategory;
import org.hanihome.hanihomebe.item.domain.OptionItem;
import org.hanihome.hanihomebe.item.repository.OptionCategoryRepository;
import org.hanihome.hanihomebe.item.repository.OptionItemRepository;
import org.hanihome.hanihomebe.property.web.dto.RentPropertyCreateRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.SharePropertyCreateRequestDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

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
        // 예시: RentPropertyCreateRequestDTO 인스턴스 생성하기
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
                null,
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
        //when
        Long id = propertyService.createProperty(dto).id();
        //then
        Assertions.assertEquals(memberId,propertyService.getPropertyById(id).memberId());

    }
}