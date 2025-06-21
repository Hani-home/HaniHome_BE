package org.hanihome.hanihomebe.property.web.dto.response;

import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.property.domain.ShareProperty;
import org.hanihome.hanihomebe.property.domain.enums.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public record SharePropertyResponseDTO(
        Long id,
        Long memberId,
        PropertySuperType kind,
        GenderPreference genderPreference,
        Region region,
        List<String> photoUrls,
        BigDecimal weeklyCost,
        List<String> optionItemNames,
        String costDescription,
        BigDecimal deposit,
        BigDecimal keyDeposit,
        Integer noticePeriodWeeks,
        Integer minimumStayWeeks,
        String contractTerms,
        Set<LocalDateTime> availableFrom,
        ParkingOption parkingOption,
        Set<LocalDateTime> possibleMeetingDates,
        String description,
        SharePropertySubType sharePropertySubType,                // 1. 매물 유형 (세컨드룸/마스터룸/거실쉐어)
        Double internalArea,                                     // 2-1. 실제 사용 면적
        Double totalArea,                                        // 2-2. 전체 면적
        Integer totalResidents,                                  // 2-3. 총 거주 인원
        Integer totalBathUser,                                   // 2-4. 욕실 공유 인원
        Integer totalFloors,                                     // 2-5. 건물 총 층수
        Integer propertyFloor,                                   // 2-6. 해당 매물의 층수
        CapacityShare capacityShare                             // 3. 수용 인원
) implements PropertyResponseDTO {
    public static SharePropertyResponseDTO from(ShareProperty shareProperty) {
        if (shareProperty == null) {
            return null;
        }

        Long extractedMemberId = (shareProperty.getMember() != null) ? shareProperty.getMember().getId() : null;

        // optionItemNames 추출
        // shareProperty.getOptionItems()가 OptionItem 객체의 컬렉션을 반환하고,
        // 각 OptionItem 객체에 이름(예: getName())을 반환하는 메서드가 있다고 가정합니다.
        // 실제 OptionItem 클래스명과 이름 추출 메서드로 수정해야 합니다.
        List<String> extractedOptionItemNames;
        if (shareProperty.getOptionItems() != null && !shareProperty.getOptionItems().isEmpty()) {
            extractedOptionItemNames = shareProperty.getOptionItems().stream()
                    // .map(ActualOptionItemClass::getName) // 예: OptionItem::getName
                    // 아래는 실제 OptionItem 클래스 구조에 맞춰 수정해야 하는 플레이스홀더입니다.
                    // OptionItem 클래스에 getName() 메서드가 있다고 가정:
                    .map(item -> {
                        // item.getName()과 같이 실제 이름 속성에 접근해야 합니다.
                        // 아래는 item이 OptionItem 타입이고 getName() 메서드를 가진다고 가정한 예시입니다.
                        // if (item instanceof YourActualOptionItemType) {
                        //     return ((YourActualOptionItemType) item).getName();
                        // }
                        return String.valueOf(item); // 임시: 실제 객체의 이름 필드를 사용하도록 수정 필요
                    })
                    .collect(Collectors.toList());
        } else {
            extractedOptionItemNames = Collections.emptyList();
        }

        // photoUrls 추출 (shareProperty.getPhotoUrls()가 List<String>을 반환한다고 가정)
        // 만약 List<PhotoObject>를 반환하고 각 PhotoObject에 getUrl() 메서드가 있다면 매핑 필요
        List<String> extractedPhotoUrls = (shareProperty.getPhotoUrls() != null) ?
                new ArrayList<>(shareProperty.getPhotoUrls()) : // 방어적 복사
                Collections.emptyList();

        Set<LocalDateTime> extractedAvailableFrom = (shareProperty.getAvailableFrom() != null) ?
                new HashSet<>(shareProperty.getAvailableFrom()) :
                Collections.emptySet();

        Set<LocalDateTime> extractedpossibleMeetingDates = (shareProperty.getPossibleMeetingDates() != null) ?
                new HashSet<>(shareProperty.getPossibleMeetingDates()) :
                Collections.emptySet();

        return new SharePropertyResponseDTO(
                shareProperty.getId(),
                extractedMemberId,
                shareProperty.getKind(),
                shareProperty.getGenderPreference(),
                shareProperty.getRegion(),
                extractedPhotoUrls,
                shareProperty.getWeeklyCost(),
                extractedOptionItemNames,
                shareProperty.getCostDescription(),
                shareProperty.getDeposit(),
                shareProperty.getKeyDeposit(),
                shareProperty.getNoticePeriodWeeks(),
                shareProperty.getMinimumStayWeeks(),
                shareProperty.getContractTerms(),
                extractedAvailableFrom,
                shareProperty.getParkingOption(),
                extractedpossibleMeetingDates,
                shareProperty.getDescription(),
                shareProperty.getSharePropertySubType(),     // 1. 매물 유형
                shareProperty.getInternalArea(),             // 2-1. 실제 사용 면적
                shareProperty.getTotalArea(),                // 2-2. 전체 면적
                shareProperty.getTotalResidents(),           // 2-3. 총 거주 인원
                shareProperty.getTotalBathUser(),            // 2-4. 욕실 공유 인원
                shareProperty.getTotalFloors(),              // 2-5. 건물 총 층수
                shareProperty.getPropertyFloor(),            // 2-6. 해당 매물의 층수
                shareProperty.getCapacityShare()             // 3. 수용 인원
        );
    }
}