package org.hanihome.hanihomebe.property.web.dto.response;
import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.domain.RentProperty;
import org.hanihome.hanihomebe.property.domain.enums.*;
import org.hanihome.hanihomebe.property.web.dto.RentPropertyPatchRequestDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public record RentPropertyResponseDTO(
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
        RentPropertySubType rentPropertySubType,    // RENT 전용 필드
        Boolean isRealEstateIntervention // RENT 전용 필드
)implements PropertyResponseDTO {
    public static RentPropertyResponseDTO from(RentProperty rentProperty) {

        // memberId 추출 (rentProperty.getMember()가 Member 객체를 반환하고, Member 객체에 getId()가 있다고 가정)
        Long memberId = (rentProperty.getMember() != null) ? rentProperty.getMember().getId() : null;


        List<String> optionItemNames;
        if (rentProperty.getOptionItems() != null && !rentProperty.getOptionItems().isEmpty()) {
            optionItemNames = rentProperty.getOptionItems().stream()
                    .map(item -> item.getOptionItemName()) // 임시: 실제 객체의 이름 필드를 사용하도록 수정 필요)
                    .collect(Collectors.toList());

        } else {
            optionItemNames = Collections.emptyList();
        }

        // photoUrls 추출 (rentProperty.getPhotoUrls()가 List<String>을 반환한다고 가정)
        List<String> photoUrls = (rentProperty.getPhotoUrls() != null) ?
                new ArrayList<>(rentProperty.getPhotoUrls()) : // 방어적 복사
                Collections.emptyList();

        // availableFrom 추출 (방어적 복사)
        Set<LocalDateTime> extractedAvailableFrom = (rentProperty.getAvailableFrom() != null) ?
                new HashSet<>(rentProperty.getAvailableFrom()) :
                Collections.emptySet();

        // viewingDates 추출 (방어적 복사)
        Set<LocalDateTime> extractedViewingDates = (rentProperty.getViewingDates() != null) ?
                new HashSet<>(rentProperty.getViewingDates()) :
                Collections.emptySet();

        return new RentPropertyResponseDTO(
                rentProperty.getId(),
                memberId,
                rentProperty.getKind(),
                rentProperty.getCapacity(),
                rentProperty.getGenderPreference(),
                rentProperty.getRegion(),
                photoUrls,
                rentProperty.getWeeklyCost(),
                optionItemNames,
                rentProperty.getCostDescription(),
                rentProperty.getDeposit(),
                rentProperty.getKeyDeposit(),
                rentProperty.getNoticePeriodWeeks(),
                rentProperty.getMinimumStayWeeks(),
                rentProperty.getContractTerms(),
                extractedAvailableFrom,
                rentProperty.getParkingOption(),
                extractedViewingDates,
                rentProperty.getDescription(),
                rentProperty.getRentPropertySubType(),
                rentProperty.isRealEstateIntervention()
        );
    }
}
