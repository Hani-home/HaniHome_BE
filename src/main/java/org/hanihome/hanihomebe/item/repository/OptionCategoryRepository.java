package org.hanihome.hanihomebe.item.repository;

import org.hanihome.hanihomebe.item.domain.CategoryCode;
import org.hanihome.hanihomebe.item.domain.OptionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OptionCategoryRepository extends JpaRepository<OptionCategory, Long> {
    boolean existsByCategoryCode(CategoryCode categoryCode);

    Optional<OptionCategory> findByCategoryCode(CategoryCode categoryCode);
}
