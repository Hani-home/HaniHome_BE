package org.hanihome.hanihomebe.item.application;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.item.domain.*;
import org.hanihome.hanihomebe.item.repository.OptionCategoryRepository;
import org.hanihome.hanihomebe.item.repository.OptionCategoryScopeRepository;
import org.hanihome.hanihomebe.item.repository.ScopeTypeRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ScopeInitializer {


    private final ScopeTypeRepository scopeTypeRepository;
    private final OptionCategoryScopeRepository categoryScopeRepository;
    private final OptionCategoryRepository categoryRepository;


    void initialize() {
        initializeScopeType();
        initializeOptionCategoryScope();
    }

    /**
     * 현재 존재하는 ScopeCode 모두 init
     */
    private void initializeScopeType() {
        for (ScopeCode scope : ScopeCode.values()) {
            if (isAlreadyInitialized(scope)) continue;

            ScopeType scopeType = ScopeType.create(scope);
            scopeTypeRepository.save(scopeType);
        }
    }

    private boolean isAlreadyInitialized(ScopeCode scope) {
        return scopeTypeRepository.existsByScopeCode(scope);
    }


    /**
     * ScopeType과 OptionCategory를 사용하여 OptionCategoryScope를 init
     */
    private void initializeOptionCategoryScope() {
        ScopeType rentScope = getScopeType(ScopeCode.SCOPE_RENT);
        ScopeType shareScope = getScopeType(ScopeCode.SCOPE_SHARE);
        ScopeType viewingScope = getScopeType(ScopeCode.SCOPE_VIEWING);

        // 매물 카테고리 - 스코프 연결
        for (CategoryCode categoryCode : getPropertyCategories()) {
            OptionCategory category = getCategory(categoryCode);
            // SCOPE_RENT 에만 연결
            if (category.getCategoryCode().equals(CategoryCode.PROPERTY_CAT5)) {
                connectCategoryToScope(category, List.of(rentScope));
                continue;
            }
            // SCOPE_RENT, SCOPE_SHARE에 연결
            connectCategoryToScope(category, List.of(rentScope, shareScope));


        }

        // 뷰잉 카테고리 - 스코프 연결
        for (CategoryCode categoryCode : getViewingCategories()) {
            OptionCategory category = getCategory(categoryCode);

            // SCOPE_VIEWING에 연결
            connectCategoryToScope(category, List.of(viewingScope));
        }
    }

    private void connectCategoryToScope(OptionCategory category, List<ScopeType> rentScope) {
        rentScope.forEach(scopeType -> {
            OptionCategoryScope categoryScope = OptionCategoryScope.create(category, scopeType);
            categoryScopeRepository.save(categoryScope);
        });
    }

    private CategoryCode[] getPropertyCategories() {
        return new CategoryCode[] {
            CategoryCode.PROPERTY_CAT1,
            CategoryCode.PROPERTY_CAT2,
            CategoryCode.PROPERTY_CAT3,
            CategoryCode.PROPERTY_CAT4,
            CategoryCode.PROPERTY_CAT5
        };
    }

    private CategoryCode[] getViewingCategories() {
        return new CategoryCode[] {
            CategoryCode.VIEWING_CAT1,
            CategoryCode.VIEWING_CAT2
        };
    }

    private OptionCategory getCategory(CategoryCode categoryCode) {
        return categoryRepository.findByCategoryCode(categoryCode)
                .orElseThrow(() -> new CustomException(ServiceCode.OPTION_CATEGORY_NOT_INITIALIZED));
    }

    private ScopeType getScopeType(ScopeCode scopeCode) {
        return scopeTypeRepository.findByScopeCode(scopeCode)
                .orElseThrow(() -> new CustomException(ServiceCode.SCOPE_TYPE_NOT_INITIALIZED));
    }


}
