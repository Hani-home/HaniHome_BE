package org.hanihome.hanihomebe.property.repository;

import org.hanihome.hanihomebe.property.domain.option.OptionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionItemRepository extends JpaRepository<OptionItem, Long> {
}
