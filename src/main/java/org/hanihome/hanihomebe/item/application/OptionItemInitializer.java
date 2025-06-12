package org.hanihome.hanihomebe.item.application;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.item.domain.CategoryCode;
import org.hanihome.hanihomebe.item.domain.OptionCategory;
import org.hanihome.hanihomebe.item.domain.OptionItem;
import org.hanihome.hanihomebe.item.repository.OptionCategoryRepository;
import org.hanihome.hanihomebe.item.repository.OptionItemRepository;

import java.util.List;


@RequiredArgsConstructor
public abstract class OptionItemInitializer {
    private final OptionCategoryRepository categoryRepository;
    private final OptionItemRepository itemRepository;


    abstract void initialize();

    /**
     * 카테고리가 이미 DB에 들어있는지 여부 검증
     * @param categoryCode : 확인 대상
     * @return :DB에 카테고리 저장 됨/안됨
     */
    protected boolean isAlreadyInitialized(CategoryCode categoryCode) {
        return categoryRepository.existsByCategoryCode(categoryCode);
    }

    /**
     * 중복없이 Category 저장하기
     * @param code :카테고리 코드
     * @return :저장된 카테고리
     */
    protected OptionCategory saveCategory(CategoryCode code) {
        return categoryRepository.findByCategoryCode(code)
                .orElseGet(() -> categoryRepository.save(OptionCategory.create(code)));
    }


    /**
     * 중복없이 OptionItem 저장하기
     * @param category :속하는 카테고리
     * @param itemName :대상 아이템
     * @return :저장된 아이템
     */
    protected OptionItem saveItemIfNotExists(OptionCategory category, String itemName) {
        return itemRepository.findByOptionCategoryAndItemName(category, itemName)
                .orElseGet(() -> itemRepository.save(OptionItem.createDefault(category, itemName)));
    }

    /**
     * OptionItem 저장하기-부모관계
     * @param category :속하는 카테고리
     * @param parentName :같은 OptionItem 이지만 상위 계층의 아이템
     * @param childrenNames :parent 아이템에 속할 대상 아이템
     */
    protected void saveParentChildItems(OptionCategory category, String parentName, List<String> childrenNames) {
        OptionItem parent = saveItemIfNotExists(category, parentName);

        childrenNames.forEach(childName -> {
            itemRepository.findByOptionCategoryAndParentAndItemName(category, parent, childName)
                    .orElseGet(() -> itemRepository.save(
                            OptionItem.createDefault(category, parent, childName)
                    ));
        });
    }
}
