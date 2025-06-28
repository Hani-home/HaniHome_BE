package org.hanihome.hanihomebe.property.application.service;

import io.github.cdimascio.dotenv.Dotenv;
import org.hanihome.hanihomebe.s3.service.S3Service;
import org.hanihome.hanihomebe.s3.web.dto.S3ResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URI;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import static org.mockito.ArgumentMatchers.any;

/*
테스트의 목적: "S3Service의 generatePresignedUrl() 메서드가 내가 원하는 대로 작동하는가?" 를 테스트
 */

public class S3ServiceTest {

    private S3Service s3Service;
    private S3Presigner mockPresigner;

    @BeforeEach
    void setUp() {
        Dotenv dotenv = Dotenv.configure()
                .filename(".env.test")
                .ignoreIfMissing()
                .load();

        String bucketName = dotenv.get("S3_BUCKET");
        String region = dotenv.get("S3_REGION");

        mockPresigner = mock(S3Presigner.class); //가짜 presigner 클래스를 만듦. 이러면 실제로 S3에 요청을 안보내고 presigned URL을 만든다.
        s3Service = new S3Service(mockPresigner, bucketName, region);
    }

    @Test
    void generatePresignedUrl()  throws Exception {
        String fileName = "profile.jpg";
        String folder = "test-folder";
        String expectedUploadUrl = "https://presigned.example.com/upload";
        String expectedFileUrl = "https://hanihome-test.s3.ap-northeast-2.amazonaws.com/test-folder/profile.jpg";

        URL fakePresignedUri = new URI(expectedUploadUrl).toURL();

        PresignedPutObjectRequest mockPresignedRequest = mock(PresignedPutObjectRequest.class);
        when(mockPresignedRequest.url()).thenReturn(fakePresignedUri); //실제로 나오는 값이랑은 다름. 우리가 지정한 값을 리턴하도록하고 그 결과를 비교할 것임.

        when(mockPresigner.presignPutObject(any(PutObjectPresignRequest.class)))
                .thenReturn(mockPresignedRequest);

        // when
        S3ResponseDTO result = s3Service.generatePresignedUrl(fileName, folder); //이건 내가 만들어둔 s3Service에서 결과를 가져옴

        // then
        assertEquals(expectedUploadUrl, result.getPresignedUrl());
        assertEquals(expectedFileUrl, result.getFileUrl()); //여기서 직접 비교해서 안되면 테스트 실패.
    }
}
