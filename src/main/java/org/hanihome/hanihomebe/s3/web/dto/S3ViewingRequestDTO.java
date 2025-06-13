package org.hanihome.hanihomebe.s3.web.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class S3ViewingRequestDTO {
    @Size(min = 1)
    List<String> fileExtensions;
}
