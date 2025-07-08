package org.hanihome.hanihomebe.report.repository;

import org.hanihome.hanihomebe.report.application.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

}
