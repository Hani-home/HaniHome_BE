package org.hanihome.hanihomebe.property.repository;

import org.hanihome.hanihomebe.property.domain.option.OptionCategory;
import org.hanihome.hanihomebe.property.domain.option.OptionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionItemRepository extends JpaRepository<OptionItem, Long> {
        Optional<OptionItem> findByOptionCategoryAndItemName(OptionCategory optionCategory, String itemName);
        Optional<OptionItem> findByOptionCategoryAndParentAndItemName(OptionCategory optionCategory, OptionItem parent, String itemName);
}

