package org.hanihome.hanihomebe.item.repository;

import org.hanihome.hanihomebe.item.domain.CategoryCode;
import org.hanihome.hanihomebe.item.domain.OptionCategory;
import org.hanihome.hanihomebe.item.domain.ScopeCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionCategoryRepository extends JpaRepository<OptionCategory, Long> {
    boolean existsByCategoryCode(CategoryCode categoryCode);

    Optional<OptionCategory> findByCategoryCode(CategoryCode categoryCode);

    @Query("select c from OptionCategory c " +
            "join OptionCategoryScope cs on cs.optionCategory = c " +
            "where cs.scopeType.scopeCode = :scopeCode")
    List<OptionCategory> findAllByScope(ScopeCode scopeCode);

}
