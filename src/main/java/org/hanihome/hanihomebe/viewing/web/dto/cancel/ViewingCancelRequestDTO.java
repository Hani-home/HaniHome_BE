package org.hanihome.hanihomebe.viewing.web.dto.cancel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ViewingCancelRequestDTO {
    private Long viewingId;
    private List<Long> cancelOptionItemIds;
    private String reason;

    public static ViewingCancelRequestDTO create(Long viewingId, List<Long> cancelOptionItemIds, String reason) {
        return new ViewingCancelRequestDTO(viewingId, cancelOptionItemIds, reason);
    }
}
