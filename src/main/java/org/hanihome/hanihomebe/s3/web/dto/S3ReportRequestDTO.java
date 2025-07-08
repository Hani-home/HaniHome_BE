package org.hanihome.hanihomebe.s3.web.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hanihome.hanihomebe.report.application.domain.ReportTargetType;

import java.util.List;

@Getter
public class S3ReportRequestDTO {

    private ReportTargetType reportTargetType;

    @Size(min = 1)
    private List<String> fileExtensions;
}
