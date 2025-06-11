package org.hanihome.hanihomebe.property.application;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.property.domain.option.CategoryCode;
import org.hanihome.hanihomebe.property.domain.option.OptionCategory;
import org.hanihome.hanihomebe.property.domain.option.OptionItem;
import org.hanihome.hanihomebe.property.repository.OptionCategoryRepository;
import org.hanihome.hanihomebe.property.repository.OptionItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OptionItemManager {
    private final OptionItemRepository itemRepository;
    private final OptionCategoryRepository categoryRepository;

    @Transactional
    @PostConstruct
    public void init() {
        initializeOptionItems();
    }

    @Transactional
    public void initializeOptionItems() {
        // 이미 초기화되어 있는지 확인
        if (isAlreadyInitialized()) {
            return;
        }

        // 공통 아이템 초기화
        initializeBillItems();                  // 카테고리1: 빌에 포함된 항목
        initializePossibleNotPossibleItems();   // 카테고리2: 가능불가능 여부
        initializeFreeToolsItems();             // 카테고리3: 무료 제공 가전가구
        initializePropertyAdvantageItems();     // 카테고리4: 매물 장점
    }

    private boolean isAlreadyInitialized() {
        return categoryRepository.existsByCategoryCode(CategoryCode.CAT1);
    }

    ///  빌에 포함된 항목
    private void initializeBillItems() {
        // save Category
        OptionCategory category = saveCategory(CategoryCode.CAT1);

        List<String> billItems = Arrays.asList(
                "수도세", "전기세", "인터넷비", "가스비", "청소비", "주차비", "직접입력"
        );

        // save item
        billItems.forEach(itemName ->
                saveItemIfNotExists(category, itemName)
        );
    }

    /// 가능 불가능 여부
    private void initializePossibleNotPossibleItems() {
        OptionCategory category = saveCategory(CategoryCode.CAT2);

        // 부모-자식 관계의 아이템 초기화
        saveParentChildItems(category, "흡연자", Arrays.asList("가능", "불가능"));
        saveParentChildItems(category, "반려동물", Arrays.asList("가능", "불가능"));
        saveParentChildItems(category, "외부인 방문", Arrays.asList("가능", "불가능"));
        saveParentChildItems(category, "주차", Arrays.asList("전용공간", "StreetParking", "불가능"));
        saveParentChildItems(category, "주방", Arrays.asList("가능", "불가능"));
    }

    /// 무료 가전,가 구
    private void initializeFreeToolsItems() {
        OptionCategory category = saveCategory(CategoryCode.CAT3);

        // 침실 아이템
        saveParentChildItems(category, "침실", Arrays.asList(
                "침대 프레임", "책상", "침구류", "옷장", "수납장", "의자"
        ));

        // 주방 아이템
        saveParentChildItems(category, "주방", Arrays.asList(
                "전자렌지", "냉장고", "가스렌지", "식기류", "조리도구"
        ));

        // 거실 아이템
        saveParentChildItems(category, "거실", Arrays.asList(
                "TV", "소파", "커피테이블"
        ));

        // 기타 아이템
        saveParentChildItems(category, "기타", Arrays.asList(
                "Wifi", "청소기"
        ));
    }

    /// 이 매물의 장점
    private void initializePropertyAdvantageItems() {
        OptionCategory category = saveCategory(CategoryCode.CAT4);

        List<String> advantages = Arrays.asList(
                "햇빛이 잘들어요", "주변 편의시설이 많아요", "전망이 좋아요",
                "주변보다 저렴해요", "테라스가 있어요", "교통이 편리해요",
                "커뮤니티 시설이 좋아요", "집상태가 깨끗해요", "방음이 잘돼요", "치안이 좋아요"
        );

        advantages.forEach(itemName ->
                saveItemIfNotExists(category, itemName)
        );
    }

    /**
     * 중복없이 Category 저장하기
     * @param code :카테고리 코드
     * @return :저장된 카테고리
     */
    private OptionCategory saveCategory(CategoryCode code) {
        return categoryRepository.findByCategoryCode(code)
                .orElseGet(() -> categoryRepository.save(OptionCategory.create(code)));
    }

    /**
     * 중복없이 OptionItem 저장하기
     * @param category :속하는 카테고리
     * @param itemName :대상 아이템
     * @return :저장된 아이템
     */
    private OptionItem saveItemIfNotExists(OptionCategory category, String itemName) {
        return itemRepository.findByOptionCategoryAndItemName(category, itemName)
                .orElseGet(() -> itemRepository.save(OptionItem.createDefault(category, itemName)));
    }

    /**
     * OptionItem 저장하기-부모관계
     * @param category :속하는 카테고리
     * @param parentName :같은 OptionItem 이지만 상위 계층의 아이템
     * @param childrenNames :parent 아이템에 속할 대상 아이템
     */
    private void saveParentChildItems(OptionCategory category, String parentName, List<String> childrenNames) {
        OptionItem parent = saveItemIfNotExists(category, parentName);

        childrenNames.forEach(childName -> {
            itemRepository.findByOptionCategoryAndParentAndItemName(category, parent, childName)
                    .orElseGet(() -> itemRepository.save(
                            OptionItem.createDefault(category, parent, childName)
                    ));
        });
    }
}