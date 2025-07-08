package org.hanihome.hanihomebe.report.application;

import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.report.application.domain.Report;
import org.hanihome.hanihomebe.report.application.domain.ReportTargetType;
import org.hanihome.hanihomebe.report.web.dto.ReportRequestDTO;

public interface ReportHandler {
    ReportTargetType getTargetType();
    Report createAndSave(Member reporter, ReportRequestDTO dto);
    boolean validate(ReportRequestDTO dto);
}
