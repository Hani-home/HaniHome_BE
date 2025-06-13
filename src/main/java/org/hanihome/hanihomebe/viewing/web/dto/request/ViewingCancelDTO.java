package org.hanihome.hanihomebe.viewing.web.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ViewingCancelDTO {
    private Long viewingId;
    private List<Long> optionItemIds;
    private String reason;
}
