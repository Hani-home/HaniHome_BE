package org.hanihome.hanihomebe.s3.service;

import lombok.RequiredArgsConstructor;

import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.s3.web.dto.S3ResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;


import java.net.URISyntaxException;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3Service {

    //Presigned URL 생성 전용 객체, Config에서 Bean으로 등록하여 주입받음
    private final S3Presigner s3Presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    //PresignedUrl 생성 메서드, 파일 이름과  폴더명을 받아 presigned URL과 실제 접근 가능한 fileUrl 생성해서 반환
    /**
     * 파일 이름과 폴더명을 받아 Presigned URL과 실제 접근 가능한 fileUrl을 생성해 반환
     * @param fileName 업로드할 파일 이름
     * @param folder S3 내 저장될 폴더 경로
     * @return uploadUrl: 프론트에서 PUT 요청할 presigned URL
     *         fileUrl: 최종 접근 가능한 정적 URL
     */
    public S3ResponseDTO generatePresignedUrl(String fileName, String folder) {
        //최종적으로 S3에 저장될 경로 ex. verification/ID_CARD/user1.jpg
        String objectKey = folder + "/" + fileName;

        //확장자 추출
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

        // contentType 매핑
        String contentType = switch (extension) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "webp" -> "image/webp";
            case "bmp" -> "image/bmp";
            case "heic" -> "image/heic";
            default -> "application/octet-stream"; // 기본 fallback
        };
        /*
        S3 객체 업로드 요청에 필요한 정보 구성
        1. 버킷 이름
        2. 저장될 경로
        3. 저장 타입
         */
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .contentType(contentType)
                .build();

        /*
        위의 요청을 기반으로 5분 동안 유효한 presigned URL을 발급받기 위한 요청 객체 생성
         */
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5)) // 유효시간
                .putObjectRequest(objectRequest)
                .build();

        //presigned URL
        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

        try {
            //presignedRequest.url().toURI() => presigned URL , fileUrl 업로드 완료 후 접근 가능한 고정 이미지 URL
            return new S3ResponseDTO(
                    presignedRequest.url().toURI().toString(),
                    "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + objectKey
            );
        } catch (URISyntaxException e) {
            throw new CustomException(ServiceCode.S3_URI_CONVERSION_ERROR, e);
        }
    }
}
