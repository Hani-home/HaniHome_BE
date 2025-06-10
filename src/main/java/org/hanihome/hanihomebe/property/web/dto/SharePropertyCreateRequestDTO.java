package org.hanihome.hanihomebe.property.web.dto;

import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.property.domain.enums.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public record SharePropertyCreateRequestDTO(
        Long memberId,                              // 소유자 회원 ID
        PropertySuperType kind,                     // 매물 종류 (SHARE / RENT)
        GenderPreference genderPreference,          // 선호 성별
        Region region,                              // 주소 정보 (Embedded 타입)
        List<String> photoUrls,                     // 매물 사진 URL 리스트
        BigDecimal weeklyCost,                      // 주 단위 비용
        List<Long> optionItemIds,                   // 포함된 비용 항목 리스트
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
        SharePropertySubType sharePropertySubType,  //고유필드 1. 매물 유형 (세컨드룸/마스터룸/거실쉐어)
        Double internalArea,                        //고유필드 2-1. 실제 사용 면적
        Double totalArea,                           //고유필드 2-2. 전체 면적
        Integer totalResidents,                     //고유필드 2-3. 총 거주 인원
        Integer totalBathUser,                      //고유필드 2-4. 욕실 공유 인원
        Integer totalFloors,                        //고유필드 2-5. 건물 총 층수
        Integer propertyFloor,                      //고유필드 2-6. 해당 매물의 층수
        CapacityShare capacityShare                 //고유필드 3. 수용 인원
) implements PropertyCreateRequestDTO {
    public SharePropertyCreateRequestDTO {
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