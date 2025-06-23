package org.hanihome.hanihomebe.admin.customerservice.repository;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.admin.customerservice.domain.OneOnOneConsult;
import org.hanihome.hanihomebe.admin.customerservice.domain.OneOnOneConsultStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OneOnOneConsultRepository extends JpaRepository<OneOnOneConsult, Long> {
    List<OneOnOneConsult> findByStatus(OneOnOneConsultStatus status);
}
