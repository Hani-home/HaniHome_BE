package org.hanihome.hanihomebe.property.web.dto;

import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.property.domain.option.OptionItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.hanihome.hanihomebe.property.domain.enums.Capacity;
import org.hanihome.hanihomebe.property.domain.enums.GenderPreference;
import org.hanihome.hanihomebe.property.domain.enums.ParkingOption;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.property.domain.enums.RentPropertySubType;
import java.util.ArrayList;
import java.util.HashSet;


/**
 * RentProperty를 생성할 때 필요한 모든 속성을 담는 DTO(record).
 * 컬렉션 필드가 null로 넘어올 경우, 생성자에서 빈 컬렉션으로 초기화하도록 처리합니다.
 */
public record RentPropertyCreateRequestDTO(
        Long memberId,                              // 소유자 회원 ID
        PropertySuperType kind,                     // 매물 종류 (SHARE / RENT)
        Capacity capacity,                          // 수용 인원
        GenderPreference genderPreference,          // 선호 성별
        Region region,                              // 주소 정보 (Embedded 타입)
        List<String> photoUrls,                     // 매물 사진 URL 리스트
        BigDecimal weeklyCost,                      // 주 단위 비용
        List<Long> optionItemIds,       // 포함된 비용 항목 리스트
        String costDescription,                     // 비용 설명 (TEXT)
        BigDecimal deposit,                         // 보증금
        BigDecimal keyDeposit,                      // 키 보증금
        Integer noticePeriodWeeks,                  // 노티스(주 단위)
        Integer minimumStayWeeks,                   // 최소 거주 기간(주 단위)
        String contractTerms,                       // 계약 형태 설명
        Set<LocalDateTime> availableFrom,           // 입주 가능일(시간 단위) 집합
        ParkingOption parkingOption,                // 주차 옵션
        Set<LocalDateTime> viewingDates,            // 뷰잉 가능 날짜 집합
        String description,                         // 매물 소개
        RentPropertySubType rentPropertySubType,    // (RentProperty 고유) 매물 유형
        boolean isRealEstateIntervention            // (RentProperty 고유) 중개 여부
)  implements PropertyCreateRequestDTO {
    public RentPropertyCreateRequestDTO {
        if (photoUrls == null) {
            photoUrls = new ArrayList<>();
        }
        if (optionItemIds == null) {
            optionItemIds = new ArrayList<>();
        }
        if (availableFrom == null) {
            availableFrom = new HashSet<>();
        }
        if (viewingDates == null) {
            viewingDates = new HashSet<>();
        }
    }
}
