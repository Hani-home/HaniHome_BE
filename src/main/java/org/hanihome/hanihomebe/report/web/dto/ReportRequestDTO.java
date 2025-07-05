package org.hanihome.hanihomebe.report.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hanihome.hanihomebe.report.application.domain.ReportTargetType;


import java.util.List;

@Getter
@AllArgsConstructor
public class ReportRequestDTO {
    private Long targetId;
    private ReportTargetType targetType;
    private List<String> documentImageUrls;
    private String description;
}
