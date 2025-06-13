package org.hanihome.hanihomebe.property.application.service;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.transaction.Transactional;
import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.member.domain.Gender;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.domain.Role;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.hanihome.hanihomebe.property.domain.enums.*;
import org.hanihome.hanihomebe.item.domain.CategoryCode;
import org.hanihome.hanihomebe.item.domain.OptionCategory;
import org.hanihome.hanihomebe.item.domain.OptionItem;
import org.hanihome.hanihomebe.item.repository.OptionCategoryRepository;
import org.hanihome.hanihomebe.item.repository.OptionItemRepository;
import org.hanihome.hanihomebe.property.web.dto.RentPropertyCreateRequestDTO;
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
        OptionCategory category = OptionCategory.create(CategoryCode.PROPERTY_CAT1);
        optionCategoryRepository.save(category);
        optionItemRepository.save(OptionItem.createDefault(category, "item1"));
        optionItemRepository.save(OptionItem.createDefault(category, "item2"));
        optionItemRepository.save(OptionItem.createDefault(category, "item3"));
        memberId = memberRepository.save(Member.builder()
                .email("test@hanihome.com")
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
        RentPropertyCreateRequestDTO dto = new RentPropertyCreateRequestDTO(
                memberId,                                   // Long memberId
                PropertySuperType.RENT,                 // PropertySuperType kind
                GenderPreference.ANY,                       // GenderPreference genderPreference
                new Region("Seoul", "Gangnam-gu", "Yeoksam-dong", "123-45", "strret", "123", "123", "building"), // Region region
                List.of("https://example.com/photo1.jpg", "https://example.com/photo2.jpg"), // List<String> photoUrls
                BigDecimal.valueOf(250),                    // BigDecimal weeklyCost
                List.of(1L, 2L, 3L),                        // List<Long> allOptionItemIds
                "물/전기/인터넷 포함",                      // String costDescription
                BigDecimal.valueOf(500),                    // BigDecimal deposit
                BigDecimal.valueOf(100),                    // BigDecimal keyDeposit
                3,                                          // Integer noticePeriodWeeks
                4,                                          // Integer minimumStayWeeks
                "최소 4주 거주 후 연장 가능",               // String contractTerms
                Set.of(LocalDateTime.of(2025, 7, 1, 10, 0)),// Set<LocalDateTime> availableFrom
                ParkingOption.RESERVED_SPACE,               // ParkingOption parkingOption
                Set.of(LocalDateTime.of(2025, 6, 10, 14, 0), LocalDateTime.of(2025, 6, 12, 10, 0)), // Set<LocalDateTime> viewingDates
                "강남역 도보 5분, 신축 3층",                 // String description
                RentPropertySubType.HOUSE,
                RealEstateType.REAL_ESTATE,
                CapacityRent.FOUR,
                Exposure.EASTERN
        );
        //when
        Long id = propertyService.createProperty(dto).id();
        //then
        Assertions.assertEquals(memberId,propertyService.getPropertyById(id).memberId());

    }
}