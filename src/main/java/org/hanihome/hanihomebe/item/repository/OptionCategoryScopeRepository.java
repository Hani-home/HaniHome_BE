package org.hanihome.hanihomebe.item.repository;

import org.hanihome.hanihomebe.item.domain.OptionCategoryScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionCategoryScopeRepository extends JpaRepository<OptionCategoryScope, Integer> {
}
