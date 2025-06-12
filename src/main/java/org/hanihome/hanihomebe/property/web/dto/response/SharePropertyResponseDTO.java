package org.hanihome.hanihomebe.property.web.dto.response;

import lombok.Getter;
import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.domain.ShareProperty;
import org.hanihome.hanihomebe.property.domain.enums.*;
import org.hanihome.hanihomebe.property.web.dto.SharePropertyPatchRequestDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public record SharePropertyResponseDTO(
        Long id,
        Long memberId,
        PropertySuperType kind,
        Capacity capacity,
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
        Set<LocalDateTime> viewingDates,
        String description,
        SharePropertySubType sharePropertySubType // SHARE 전용 필드
) implements PropertyResponseDTO {
    public static SharePropertyResponseDTO from(ShareProperty shareProperty) {
        if (shareProperty == null) {
            // 필요에 따라 null 대신 IllegalArgumentException을 던질 수 있습니다.
            // 예: throw new IllegalArgumentException("shareProperty는 null일 수 없습니다.");
            return null;
        }

        // memberId 추출 (shareProperty.getMember()가 Member 객체를 반환하고, Member 객체에 getId()가 있다고 가정)
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

        // availableFrom 추출 (방어적 복사)
        Set<LocalDateTime> extractedAvailableFrom = (shareProperty.getAvailableFrom() != null) ?
                new HashSet<>(shareProperty.getAvailableFrom()) :
                Collections.emptySet();

        // viewingDates 추출 (방어적 복사)
        Set<LocalDateTime> extractedViewingDates = (shareProperty.getViewingDates() != null) ?
                new HashSet<>(shareProperty.getViewingDates()) :
                Collections.emptySet();

        return new SharePropertyResponseDTO(
                shareProperty.getId(),
                extractedMemberId,
                shareProperty.getKind(),
                shareProperty.getCapacity(),
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
                extractedViewingDates,
                shareProperty.getDescription(),
                shareProperty.getSharePropertySubType() // ShareProperty 전용 필드
        );
    }
}
