package org.hanihome.hanihomebe.s3.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class S3RequestDTO {
    private String fileName;
    private String folder;
}
