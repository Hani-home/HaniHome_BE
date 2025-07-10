package org.hanihome.hanihomebe.viewing.web.dto.note;

import lombok.Builder;
import lombok.Getter;
import org.hanihome.hanihomebe.viewing.domain.Viewing;

import java.util.List;


@Getter
@Builder
public class ViewingNotesResponseDTO {
    private Long viewingId;
    private List<String> fileUrls;
    private String memo;
    public static ViewingNotesResponseDTO from(Viewing viewing) {
        return ViewingNotesResponseDTO.builder()
                .viewingId(viewing.getId())
                .fileUrls(viewing.getPhotoUrls())
                .memo(viewing.getMemo())
                .build();
    }
}
