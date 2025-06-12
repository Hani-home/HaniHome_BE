package org.hanihome.hanihomebe.viewing.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.hanihome.hanihomebe.viewing.domain.Viewing;
import org.hanihome.hanihomebe.viewing.domain.ViewingOptionItem;
import org.hanihome.hanihomebe.viewing.domain.ViewingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Viewing 엔티티를 API 응답으로 변환하는 DTO
 */
@Getter
@Builder
public class ViewingResponseDTO {
    private final Long id;
    private final Long memberId;
    private final Long propertyId;
    private final LocalDateTime meetingDay;
    private final ViewingStatus status;
    private final String cancelReason;
    private final List<String> photoUrls;
    private final String memo;
    private final List<String> optionItemNames;

    /**
     * Viewing 엔티티를 DTO로 변환
     */
    public static ViewingResponseDTO from(Viewing viewing) {
        List<String> photoUrls = viewing.getPhotoUrls() == null
                ? List.of()
                : List.copyOf(viewing.getPhotoUrls());

        List<String> optionItemNames = viewing.getViewingOptionItems() == null
                ? List.of()
                : viewing.getViewingOptionItems().stream()
                .map(ViewingOptionItem::getOptionItemName)
                .collect(Collectors.toList());

        return ViewingResponseDTO.builder()
                .id(viewing.getId())
                .memberId(viewing.getMember().getId())
                .propertyId(viewing.getProperty().getId())
                .meetingDay(viewing.getMeetingDay())
                .status(viewing.getStatus())
                .cancelReason(viewing.getCancelReason())
                .photoUrls(photoUrls)
                .memo(viewing.getMemo())
                .optionItemNames(optionItemNames)
                .build();
    }
}

