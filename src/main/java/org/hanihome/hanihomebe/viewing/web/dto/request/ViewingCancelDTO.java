package org.hanihome.hanihomebe.viewing.web.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ViewingCancelDTO {
    private Long viewingId;

    private String reason;
}
