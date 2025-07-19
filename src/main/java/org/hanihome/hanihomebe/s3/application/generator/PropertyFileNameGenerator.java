package org.hanihome.hanihomebe.s3.application.generator;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class PropertyFileNameGenerator implements FileNameGenerator {
    @Override
    public String generate(FileNameContext fileNameContext) {
        String fileName = "property" + "-" + fileNameContext.getMemberId() + "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-" + UUID.randomUUID() + "." + fileNameContext.getExtension();
        return fileName;
    }
}
