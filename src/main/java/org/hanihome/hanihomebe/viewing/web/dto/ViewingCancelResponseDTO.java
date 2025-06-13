package org.hanihome.hanihomebe.viewing.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class ViewingCancelResponseDTO {
    private Long viewingId;
    private List<Long> optionItemIds;
    private String reason;

    public static ViewingCancelResponseDTO from(Long viewingId, List<Long> cancelReasonItemIds, String reason) {
        return ViewingCancelResponseDTO.builder()
                .viewingId(viewingId)
                .optionItemIds(cancelReasonItemIds)
                .reason(reason)
                .build();
    }
}