package org.hanihome.hanihomebe.property.repository;

import org.hanihome.hanihomebe.property.domain.option.CategoryCode;
import org.hanihome.hanihomebe.property.domain.option.OptionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionCategoryRepository extends JpaRepository<OptionCategory, Long> {
    boolean existsByCategoryCode(CategoryCode categoryCode);
    Optional<OptionCategory> findByCategoryCode(CategoryCode categoryCode);
}
