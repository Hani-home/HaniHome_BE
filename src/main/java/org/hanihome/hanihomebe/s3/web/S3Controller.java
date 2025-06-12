package org.hanihome.hanihomebe.s3.web;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.s3.web.dto.S3RequestDTO;
import org.hanihome.hanihomebe.s3.web.dto.S3ResponseDTO;
import org.hanihome.hanihomebe.s3.service.S3Service;
import org.hanihome.hanihomebe.s3.web.dto.S3VerificationRequestDTO;
import org.hanihome.hanihomebe.security.auth.user.detail.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/s3")
public class S3Controller {

    private final S3Service s3Service;

    //테슽트용
    @PostMapping("/presigned-url")
    public ResponseEntity<S3ResponseDTO> getPresignedUrl(@RequestBody S3RequestDTO s3RequestDTO) {
        var result = s3Service.generatePresignedUrl(s3RequestDTO.getFileName(), s3RequestDTO.getFolder());

        return ResponseEntity.ok(
                new S3ResponseDTO(
                        result.uploadUrl().toString(),
                        result.fileUrl().toString()
                )
        );
    }

    @PostMapping("/verifications/presigned-url")
    public ResponseEntity<S3ResponseDTO> getVerificationPresignedUrl(@RequestBody S3VerificationRequestDTO s3VerificationRequestDTO, @AuthenticationPrincipal CustomUserDetails userDetails) {
        //s3 저장 폴더구조 verification/passport-{memberId}-(저장된 시간)
        String fileName = s3VerificationRequestDTO.getType() + "-" + userDetails.getUserId() + "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        S3Service.PresignedUploadResponse result = s3Service.generatePresignedUrl(fileName, "verification");

        return ResponseEntity.ok(new S3ResponseDTO(
                result.uploadUrl().toString(),
                result.fileUrl().toString()
        ));
    }


}
