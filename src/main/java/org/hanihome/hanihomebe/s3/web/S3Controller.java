package org.hanihome.hanihomebe.s3.web;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.s3.web.dto.S3RequestDTO;
import org.hanihome.hanihomebe.s3.web.dto.S3ResponseDTO;
import org.hanihome.hanihomebe.s3.service.S3Service;
import org.hanihome.hanihomebe.s3.web.dto.S3VerificationRequestDTO;
import org.hanihome.hanihomebe.s3.web.dto.S3ViewingRequestDTO;
import org.hanihome.hanihomebe.security.auth.user.detail.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/s3")
public class S3Controller {

    private final S3Service s3Service;


    /*
    @PostMapping("/verifications/presigned-url")
    public ResponseEntity<S3ResponseDTO> getVerificationPresignedUrl(@RequestBody S3VerificationRequestDTO s3VerificationRequestDTO, @AuthenticationPrincipal CustomUserDetails userDetails) {
        //s3 저장 폴더구조 verification/passport-{memberId}-(저장된 시간)
        String fileName = s3VerificationRequestDTO.getType() + "-" + userDetails.getUserId() + "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-" + UUID.randomUUID();
        S3Service.PresignedUploadResponse result = s3Service.generatePresignedUrl(fileName, "verification");

        return ResponseEntity.ok(new S3ResponseDTO(
                result.uploadUrl().toString(),
                result.fileUrl().toString()
        ));
    }
     */

    @PostMapping("/verifications/presigned-url")
    public List<S3ResponseDTO> getVerificationsPresignedUrl(@RequestBody S3VerificationRequestDTO dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<S3ResponseDTO> dtos = dto.getFileExtensions().stream()
                .map(extension -> {
                    String fileName = dto.getType() + "-" + userDetails.getUserId() + "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-" + UUID.randomUUID() + "." + extension;

                    return s3Service.generatePresignedUrl(fileName, "verification");
                }).toList();

        return dtos;
    }

    /**
     *
     * @param dto :이미지 확장자
     * @param userDetails :인증정보
     * @return :presigned-url, fileUrl(s3 저장 경로)
     */
    @PostMapping("/viewings/property-notes/presigned-url")
    public List<S3ResponseDTO> getViewingPresignedUrl(@RequestBody S3ViewingRequestDTO dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<S3ResponseDTO> dtos = dto.getFileExtensions().stream()
                .map(extension -> {
                    String fileName = "viewing-note" + "-" + userDetails.getUserId() + "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-" + UUID.randomUUID() + "." + extension;
                    return s3Service.generatePresignedUrl(fileName, "viewings/notes");

                }).toList();

        return dtos;
    }
}
