package org.hanihome.hanihomebe.s3.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class S3ResponseDTO {
    private String presignedUrl; // 프론트가 PUT 요청할 presigned URL
    private String fileUrl;   // 최종 접근 가능한 이미지 URL
}
