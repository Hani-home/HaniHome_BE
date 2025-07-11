package org.hanihome.hanihomebe.item.application.init;

import org.hanihome.hanihomebe.item.domain.CategoryCode;
import org.hanihome.hanihomebe.item.domain.OptionCategory;
import org.hanihome.hanihomebe.item.repository.OptionCategoryRepository;
import org.hanihome.hanihomebe.item.repository.OptionItemRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class PropertyItemInitializer extends OptionItemInitializer {

    public PropertyItemInitializer(OptionCategoryRepository categoryRepository, OptionItemRepository itemRepository) {
        super(categoryRepository, itemRepository);
    }

    @Override
    public void initialize() {
        // 매물 공통 아이템 초기화
        initializePropertyAdvantageItems();     // 카테고리1: 매물 장점
        initializeFreeToolsItems();             // 카테고리2: 무료 제공 가전가구
        initializePossibleNotPossibleItems();   // 카테고리3: 가능불가능 여부
        initializeBillItems();                  // 카테고리4: 빌에 포함된 항목
        initializeRealEstateIntervention();     // 카테고리5: 부동산 중개 여부
    }


    ///  빌에 포함된 항목
    private void initializeBillItems() {
        if(super.isAlreadyInitialized(CategoryCode.PROPERTY_CAT4)) return;
        
        // save Category
        OptionCategory category = super.saveCategory(CategoryCode.PROPERTY_CAT4);

        // save item
        List<String> billItems = Arrays.asList(
                "수도세", "전기세", "인터넷비", "가스비", "청소비", "주차비", "직접입력"
        );
        billItems.forEach(itemName ->
                super.saveItemIfNotExists(category, itemName)
        );
    }

    /// 가능 불가능 여부
    private void initializePossibleNotPossibleItems() {
        if(super.isAlreadyInitialized(CategoryCode.PROPERTY_CAT3)) return;
        
        // save category
        OptionCategory category = super.saveCategory(CategoryCode.PROPERTY_CAT3);

        // save 부모-자식 관계의 아이템
        super.saveParentChildItems(category, "흡연자", Arrays.asList("가능", "불가능"));
        super.saveParentChildItems(category, "반려동물", Arrays.asList("가능", "불가능"));
        super.saveParentChildItems(category, "외부인 방문", Arrays.asList("가능", "불가능"));
        super.saveParentChildItems(category, "주차", Arrays.asList("전용공간", "StreetParking", "불가능"));
        super.saveParentChildItems(category, "주방", Arrays.asList("가능", "불가능"));
    }

    /// 무료 가전,가 구
    private void initializeFreeToolsItems() {
        if(super.isAlreadyInitialized(CategoryCode.PROPERTY_CAT2)) return;
        
        // save category
        OptionCategory category = super.saveCategory(CategoryCode.PROPERTY_CAT2);
        
        // save items
        // 침실 아이템
        super.saveParentChildItems(category, "침실", Arrays.asList(
                "침대 프레임", "책상", "침구류", "옷장", "수납장", "의자"
        ));

        // 주방 아이템
        super.saveParentChildItems(category, "주방", Arrays.asList(
                "전자렌지", "냉장고", "가스렌지", "식기류", "조리도구"
        ));

        // 거실 아이템
        super.saveParentChildItems(category, "거실", Arrays.asList(
                "TV", "소파", "커피테이블"
        ));

        // 기타 아이템
        super.saveParentChildItems(category, "기타", Arrays.asList(
                "Wifi", "청소기"
        ));
    }

    /// 이 매물의 장점
    private void initializePropertyAdvantageItems() {
        if(super.isAlreadyInitialized(CategoryCode.PROPERTY_CAT1)) return;
        
        // save category
        OptionCategory category = super.saveCategory(CategoryCode.PROPERTY_CAT1);

        // save items
        List<String> advantages = Arrays.asList(
                "햇빛이 잘들어요", "주변 편의시설이 많아요", "전망이 좋아요",
                "주변보다 저렴해요", "테라스가 있어요", "교통이 편리해요",
                "커뮤니티 시설이 좋아요", "집상태가 깨끗해요", "방음이 잘돼요", "치안이 좋아요"
        );

        advantages.forEach(itemName ->
                super.saveItemIfNotExists(category, itemName)
        );
    }

    /// 부동산 중개 여부
    private void initializeRealEstateIntervention() {
        if(super.isAlreadyInitialized(CategoryCode.PROPERTY_CAT5)) return;

        // save category
        OptionCategory category = super.saveCategory(CategoryCode.PROPERTY_CAT5);

        // save items
        List<String> items = Arrays.asList(
                "개인 임대",
                "부동산 중개"
        );
        items.forEach(itemName ->
                super.saveItemIfNotExists(category, itemName)
        );
    }
}