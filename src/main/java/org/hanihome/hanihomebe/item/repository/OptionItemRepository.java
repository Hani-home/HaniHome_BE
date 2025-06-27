package org.hanihome.hanihomebe.item.repository;

import org.hanihome.hanihomebe.item.domain.CategoryCode;
import org.hanihome.hanihomebe.item.domain.OptionCategory;
import org.hanihome.hanihomebe.item.domain.OptionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface OptionItemRepository extends JpaRepository<OptionItem, Long> {
    Optional<OptionItem> findByOptionCategoryAndItemName(OptionCategory optionCategory, String itemName);

    Optional<OptionItem> findByOptionCategoryAndParentAndItemName(OptionCategory optionCategory, OptionItem parent, String itemName);

    Optional<OptionItem> findByOptionCategoryAndParentIsNullAndItemName(OptionCategory optionCategory, String itemName);

    List<OptionItem> findAllByOptionCategory_CategoryCode(CategoryCode optionCategoryCategoryCode);

}

