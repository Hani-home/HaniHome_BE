package org.hanihome.hanihomebe.s3.application.generator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class FileNameContext {
    private Long memberId;
    private String extension;

    public static FileNameContext create(Long memberId, String extension) {
        return FileNameContext.builder()
                .memberId(memberId)
                .extension(extension)
                .build();
    }
}
