package org.hanihome.hanihomebe.viewing.web.dto;

import java.util.List;


public record ViewingNotesRequestDTO(
        Long viewingId,
        List<String> fileUrls,
        String memo
) {

}
