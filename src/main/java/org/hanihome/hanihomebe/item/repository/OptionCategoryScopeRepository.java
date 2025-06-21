package org.hanihome.hanihomebe.item.repository;

import org.hanihome.hanihomebe.item.domain.CategoryCode;
import org.hanihome.hanihomebe.item.domain.OptionCategory;
import org.hanihome.hanihomebe.item.domain.OptionCategoryScope;
import org.hanihome.hanihomebe.item.domain.ScopeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionCategoryScopeRepository extends JpaRepository<OptionCategoryScope, Integer> {
    boolean existsByOptionCategory_CategoryCodeAndScopeType(CategoryCode categoryCode, ScopeType scopeType);
}
