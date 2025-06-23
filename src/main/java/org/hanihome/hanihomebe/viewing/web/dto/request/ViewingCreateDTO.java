package org.hanihome.hanihomebe.viewing.web.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hanihome.hanihomebe.viewing.domain.ViewingStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ViewingCreateDTO {

    @NotNull(message = "매물 ID는 필수입니다.")
    private Long propertyId;
    
    @NotNull(message = "선호 시간은 필수입니다.")
    @Size(min = 1, max = 3, message = "선호 시간은 1~3개까지 선택 가능합니다.")
    private List<LocalDateTime> preferredTimes;
}