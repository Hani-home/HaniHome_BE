package org.hanihome.hanihomebe.viewing.web.dto.cancel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class ViewingCancelResponseDTO {
    private Long viewingId;
    private List<OptionItemResponseDTO> cancelReasonOptionItems;
    private String reason;

    public static ViewingCancelResponseDTO from(Long viewingId, List<OptionItemResponseDTO> cancelReasonOptionItems, String reason) {
        return ViewingCancelResponseDTO.builder()
                .viewingId(viewingId)
                .cancelReasonOptionItems(cancelReasonOptionItems)
                .reason(reason)
                .build();
    }
}