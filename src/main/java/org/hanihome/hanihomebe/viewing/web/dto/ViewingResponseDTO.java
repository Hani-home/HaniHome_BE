package org.hanihome.hanihomebe.viewing.web.dto;

import lombok.Builder;
import lombok.Getter;
import org.hanihome.hanihomebe.viewing.domain.Viewing;
import org.hanihome.hanihomebe.viewing.domain.ViewingOptionItem;
import org.hanihome.hanihomebe.viewing.domain.ViewingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Builder
public class ViewingResponseDTO implements ViewingDTOByView {
    private final Long id;

    private final Long guestId;

    private final Long hostId;

    private final Long propertyId;

    private final LocalDateTime meetingDay;

    private final ViewingStatus status;

    private final String cancelReason;

    private final List<String> photoUrls; // 매물 노트 - 사진

    private final String memo;            // 매물 노트 - 메모

    private final List<String> optionItemNames; // 체크리스트


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
                .guestId(viewing.getMember().getId())
                .hostId(viewing.getProperty().getMember().getId())
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

