package org.hanihome.hanihomebe.item.application.init;

import org.hanihome.hanihomebe.item.domain.CategoryCode;
import org.hanihome.hanihomebe.item.domain.OptionCategory;
import org.hanihome.hanihomebe.item.repository.OptionCategoryRepository;
import org.hanihome.hanihomebe.item.repository.OptionItemRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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



    /// 이 매물의 장점
    private void initializePropertyAdvantageItems() {
        saveCategoryAndOnlyParentItem(CategoryCode.PROPERTY_CAT1,
                Arrays.asList(
                "햇빛이 잘들어요", "주변 편의시설이 많아요", "전망이 좋아요",
                "주변보다 저렴해요", "테라스가 있어요", "교통이 편리해요",
                "커뮤니티 시설이 좋아요", "집상태가 깨끗해요", "방음이 잘돼요", "치안이 좋아요"
        ));
    }

    /// 무료 가전,가 구
    private void initializeFreeToolsItems() {
        CategoryCode category = CategoryCode.PROPERTY_CAT2;

        Map<String, List<String>> items = new HashMap<>();
        items.put("침실", List.of("침대 프레임", "책상", "침구류", "옷장", "수납장", "의자"));
        items.put("주방", List.of("전자렌지", "냉장고", "가스렌지", "식기류", "조리도구"));
        items.put("거실", List.of("TV", "소파", "커피테이블"));
        items.put("기타", List.of("Wifi", "청소기", "에어컨", "엘레베이터"));

        saveCategoryAndFamilyItems(category, items);
    }

    /// 가능 불가능 여부
    private void initializePossibleNotPossibleItems() {
        CategoryCode category = CategoryCode.PROPERTY_CAT3;

        Map<String, List<String>> items = new HashMap<>();
        items.put("흡연자",      List.of("가능", "불가능"));
        items.put("반려동물",    List.of("가능", "불가능"));
        items.put("외부인 방문",  List.of("가능", "불가능"));
        items.put("주차",        List.of("전용공간", "StreetParking", "불가능"));
        items.put("주방",        List.of("가능", "불가능"));

        saveCategoryAndFamilyItems(category, items);
    }

    ///  빌에 포함된 항목
    private void initializeBillItems() {
        saveCategoryAndOnlyParentItem(CategoryCode.PROPERTY_CAT4, Arrays.asList(
                "수도세", "전기세", "인터넷비", "가스비", "청소비", "주차비", "직접입력"
        ));
    }

    /// 부동산 중개 여부
    private void initializeRealEstateIntervention() {
        saveCategoryAndOnlyParentItem(CategoryCode.PROPERTY_CAT5, Arrays.asList(
                "개인 임대",
                "부동산 중개"
        ));
    }

    // 카테고리 저장, 부모자식 관계의 아이템을 저장
    private void saveCategoryAndFamilyItems(CategoryCode categoryCode,
                                            Map<String, List<String>> parentChildMap) {
        // save category
        if (super.isAlreadyInitialized(categoryCode)) {
            return;
        }

        // save 부모-자식 관계의 아이템
        OptionCategory category = super.saveCategory(categoryCode);
        parentChildMap.forEach(
                (parentName, childNames) ->
                        super.saveParentChildItems(category, parentName, childNames)
        );
    }

    // 카테고리 저장, 부모만 있는 아이템을 저장
    private void saveCategoryAndOnlyParentItem(CategoryCode categoryCode, List<String> List) {
        if (super.isAlreadyInitialized(categoryCode)) return;

        OptionCategory category = super.saveCategory(categoryCode);

        List<String> items = List;

        items.forEach(itemName ->
                super.saveItemIfNotExists(category, itemName)
        );
    }
}