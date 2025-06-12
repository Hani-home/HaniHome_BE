package org.hanihome.hanihomebe.item.repository;

import org.hanihome.hanihomebe.item.domain.OptionCategory;
import org.hanihome.hanihomebe.item.domain.OptionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OptionItemRepository extends JpaRepository<OptionItem, Long> {
        Optional<OptionItem> findByOptionCategoryAndItemName(OptionCategory optionCategory, String itemName);
        Optional<OptionItem> findByOptionCategoryAndParentAndItemName(OptionCategory optionCategory, OptionItem parent, String itemName);
}

