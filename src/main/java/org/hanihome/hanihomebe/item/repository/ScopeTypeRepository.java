package org.hanihome.hanihomebe.item.repository;

import org.hanihome.hanihomebe.item.domain.ScopeCode;
import org.hanihome.hanihomebe.item.domain.ScopeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScopeTypeRepository extends JpaRepository<ScopeType, Long> {
    boolean existsByScopeCode(ScopeCode scopeCode);

    Optional<ScopeType> findByScopeCode(ScopeCode scopeCode);
}
