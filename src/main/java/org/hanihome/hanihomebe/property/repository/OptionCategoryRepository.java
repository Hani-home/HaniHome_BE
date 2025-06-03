package org.hanihome.hanihomebe.property.repository;

import org.hanihome.hanihomebe.property.domain.option.OptionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionCategoryRepository extends JpaRepository<OptionCategory, Long> {
}
