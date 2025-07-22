package org.hanihome.hanihomebe.s3.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class S3PropertyRequestDTO {
    List<String> fileExtensions;
}
