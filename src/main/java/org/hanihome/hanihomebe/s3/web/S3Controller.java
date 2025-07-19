package org.hanihome.hanihomebe.s3.web;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.s3.application.generator.FileNameContext;
import org.hanihome.hanihomebe.s3.application.generator.FileNameGenerator;
import org.hanihome.hanihomebe.s3.application.generator.PropertyFileNameGenerator;
import org.hanihome.hanihomebe.s3.application.generator.ViewingNoteFileNameGenerator;
import org.hanihome.hanihomebe.s3.web.dto.*;
import org.hanihome.hanihomebe.s3.application.service.S3Service;
import org.hanihome.hanihomebe.security.auth.user.detail.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/s3")
public class S3Controller {

    private final S3Service s3Service;
    // TODO: FileNameGenerator, S3Service를 동일 계층으로 두고 Facade로 조립하는게 나을듯.
    //  S3Service는 그리고 PresignedUrlGenerator로 바꾸는게 나아보임
    private final List<FileNameGenerator> fileNameGenerators;
    private final Map<Class<? extends FileNameGenerator>, FileNameGenerator> fileNameGeneratorMap = new HashMap<>();


    @PostConstruct
    public void init() {
        fileNameGenerators.forEach(fileNameGenerator ->
                fileNameGeneratorMap.put(fileNameGenerator.getClass(), fileNameGenerator));
    }

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
    public List<S3ResponseDTO> getViewingPresignedUrl(@RequestBody S3ViewingRequestDTO dto,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<String> fileNames = getFileNames(userDetails, dto.getFileExtensions(), ViewingNoteFileNameGenerator.class);

        return s3Service.generatePresignedUrls(fileNames, "viewings/notes");
    }

    @PostMapping("/properties/presigned-url")
    public List<S3ResponseDTO> getPropertyPresignedUrl(@RequestBody S3PropertyRequestDTO dto,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<String> fileNames = getFileNames(userDetails, dto.getFileExtensions(), PropertyFileNameGenerator.class);

        return s3Service.generatePresignedUrls(fileNames, "properties");
    }

    private List<String> getFileNames(CustomUserDetails userDetails, List<String> extensions, Class<? extends FileNameGenerator> clazz ) {
        FileNameGenerator generator = fileNameGeneratorMap.get(clazz);
        return extensions.stream()
                .map(extension ->
                        generator.generate(new FileNameContext(userDetails.getUserId(), extension)))
                .toList();
    }

    // 아래 함수들은 일단 따로 Generator도 안만들고 해서 그냥 냅뒀읍니다.
    // 위에 코드 먼저 확인되면 나중에 리팩토링 같이 진행할게요 :)
    @PostMapping("/profiles/presigned-url")
    public S3ResponseDTO getProfilePresignedUrl(@RequestBody S3ProfileRequestDTO dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        String extension = dto.getFileExtension();
        String fileName = userDetails.getUserId() + "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + UUID.randomUUID() + "." + extension;
        return s3Service.generatePresignedUrl(fileName, "profile");
    }

    @PostMapping("/reports/presigned-url")//파일 이름에 신고에 대한 정보를 더 담고 싶은데 담을 수 가 없네...
    public List<S3ResponseDTO> getReportPresignedUrl(@RequestBody S3ReportRequestDTO dto) {
        List<S3ResponseDTO> dtos = dto.getFileExtensions().stream()
                .map(extension -> {
                    String fileName = dto.getReportTargetType() + "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + UUID.randomUUID() + "." + extension;
                    return s3Service.generatePresignedUrl(fileName, "report");
                }).toList();

        return dtos;
    }
}
